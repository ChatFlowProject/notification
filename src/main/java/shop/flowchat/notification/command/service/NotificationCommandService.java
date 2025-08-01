package shop.flowchat.notification.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.common.dto.info.MemberInfo;
import shop.flowchat.notification.common.util.JwtTokenProvider;
import shop.flowchat.notification.domain.member.MemberReadModel;
import shop.flowchat.notification.domain.notification.Notification;
import shop.flowchat.notification.domain.notification.NotificationType;
import shop.flowchat.notification.infrastructure.repository.notification.NotificationRepository;
import shop.flowchat.notification.query.MemberReadModelQuery;
import shop.flowchat.notification.query.NotificationQuery;
import shop.flowchat.notification.external.sse.dto.FriendAcceptSseEvent;
import shop.flowchat.notification.external.sse.dto.FriendRequestSseEvent;
import shop.flowchat.notification.external.sse.dto.SseEventPayload;
import shop.flowchat.notification.external.sse.repository.SseEmitterRepository;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationCommandService {
    private final NotificationQuery notificationQuery;
    private final NotificationRepository notificationRepository;
    private final MemberReadModelQuery memberReadModelQuery;
    private final SseEmitterRepository sseEmitterRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private void createNotification(
            UUID senderId,
            UUID receiverId,
            NotificationType type,
            Function<MemberReadModel, String> messageProvider,
            Function<MemberReadModel, SseEventPayload> sseEventFactory
    ) {
        MemberReadModel sender = memberReadModelQuery.getMemberById(senderId);
        Notification notification = Notification.create(
                sender,
                receiverId,
                type,
                messageProvider.apply(sender),
                null,
                null
        );
        notificationRepository.save(notification);
        sseEmitterRepository.send(sseEventFactory.apply(sender));
    }

    public void createFriendRequestNoti(UUID senderId, UUID receiverId) {
        createNotification(
                senderId,
                receiverId,
                NotificationType.FRIEND_REQUEST,
                sender -> String.format("%s 님이 친구 요청을 보냈어요.", sender.getName()),
                sender -> FriendRequestSseEvent.from(MemberInfo.from(sender), receiverId)
        );
    }

    public void createFriendAcceptNoti(UUID senderId, UUID receiverId) {
        createNotification(
                senderId,
                receiverId,
                NotificationType.FRIEND_ACCEPTED,
                sender -> String.format("%s 님이 친구 요청을 수락했어요.", sender.getName()),
                sender -> FriendAcceptSseEvent.from(MemberInfo.from(sender), receiverId)
        );
    }

    public void markNotificationAsRead(String token, Long notificationId) {
        UUID memberId = jwtTokenProvider.getMemberIdFromToken(token);
        Notification notification = notificationQuery.getNotificationById(notificationId);

        if (!notification.getReceiverId().equals(memberId)) {
            throw new IllegalArgumentException("해당 알림 읽음 처리 권한이 없습니다. : " + notificationId);
        }

        notification.markAsRead();
    }

    public void markNotificationsAsRead(String token, List<Long> notificationIds) {
        UUID memberId = jwtTokenProvider.getMemberIdFromToken(token);
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);

        List<Long> unauthorizedNotifications = notifications.stream()
                .filter(n -> !n.getReceiverId().equals(memberId))
                .map(Notification::getId)
                .toList();

        if (!unauthorizedNotifications.isEmpty()) {
            throw new IllegalArgumentException("읽음 처리 권한이 없는 알림이 있습니다. ID: " + unauthorizedNotifications);
        }

        notifications.forEach(Notification::markAsRead);
    }

    public void markAllNotificationsAsRead(String token) {
        UUID memberId = jwtTokenProvider.getMemberIdFromToken(token);
        List<Notification> unreadNotifications = notificationRepository.findAllByReceiverIdAndIsRead(memberId, false);

        unreadNotifications.forEach(Notification::markAsRead);
    }

    public void deleteNotification(String token, Long notificationId) {
        UUID memberId = jwtTokenProvider.getMemberIdFromToken(token);
        Notification notification = notificationQuery.getNotificationById(notificationId);
        if (!notification.getReceiverId().equals(memberId)) {
            throw new IllegalArgumentException("해당 알림 삭제 권한이 없습니다. : " + notificationId);
        }
        notificationRepository.delete(notification);
    }

    public void deleteReadNotifications(String token) {
        UUID memberId = jwtTokenProvider.getMemberIdFromToken(token);

        List<Notification> readNotifications = notificationRepository.findAllByReceiverIdAndIsRead(memberId, true);
        if (readNotifications.isEmpty()) return;

        notificationRepository.deleteAllInBatch(readNotifications);
    }

    public void deleteAllNotifications(String token) {
        UUID memberId = jwtTokenProvider.getMemberIdFromToken(token);
        notificationRepository.deleteAllByReceiverId(memberId);
    }

}
