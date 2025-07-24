package shop.flowchat.notification.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.common.dto.MemberInfo;
import shop.flowchat.notification.domain.member.MemberReadModel;
import shop.flowchat.notification.domain.notification.Notification;
import shop.flowchat.notification.domain.notification.NotificationType;
import shop.flowchat.notification.infrastructure.repository.notification.NotificationRepository;
import shop.flowchat.notification.query.MemberReadModelQuery;
import shop.flowchat.notification.sse.dto.FriendAcceptSseEvent;
import shop.flowchat.notification.sse.dto.FriendRequestSseEvent;
import shop.flowchat.notification.sse.dto.SseEventPayload;
import shop.flowchat.notification.sse.repository.SseEmitterRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationCommandService {
    private final NotificationRepository notificationRepository;
    private final MemberReadModelQuery memberReadModelQuery;
    private final SseEmitterRepository sseEmitterRepository;

    private void createNotification(
            UUID senderId,
            UUID receiverId,
            NotificationType type,
            Function<MemberReadModel, String> messageProvider,
            Function<MemberReadModel, SseEventPayload> sseEventFactory
    ) {
        List<MemberReadModel> members = memberReadModelQuery.getAllMembersById(List.of(senderId, receiverId));
        Map<UUID, MemberReadModel> memberMap = members.stream()
                .collect(Collectors.toMap(MemberReadModel::getId, Function.identity()));

        MemberReadModel sender = memberMap.get(senderId);
        MemberReadModel receiver = memberMap.get(receiverId);

        Notification notification = Notification.create(
                sender,
                receiver,
                type,
                messageProvider.apply(sender),
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

}
