package shop.flowchat.notification.query;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.common.util.JwtTokenProvider;
import shop.flowchat.notification.domain.notification.Notification;
import shop.flowchat.notification.infrastructure.repository.notification.NotificationRepository;
import shop.flowchat.notification.presentation.dto.CursorResponse;
import shop.flowchat.notification.presentation.dto.NotificationResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQuery {
    private final NotificationRepository notificationRepository;
    private final JwtTokenProvider jwtTokenProvider;
    
    public Notification getNotificationById(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 알림을 찾을 수 없습니다 : " + notificationId));
    }

    public CursorResponse<NotificationResponse> getAllNotifications(String token, LocalDateTime dateTime, Long notificationId, int size) {
        UUID memberId = jwtTokenProvider.getMemberIdFromToken(token);
        LocalDateTime createdAt = dateTime != null ? dateTime : LocalDateTime.now();
        Long id = notificationId != null ? notificationId : Long.MAX_VALUE;

        List<Notification> notifications = notificationRepository.findNextPageByReceiverId(
                memberId, createdAt, id, PageRequest.of(0, size + 1)
        );

        return getNotificationCursorResponse(notifications, size);
    }

    public CursorResponse<NotificationResponse> getUnreadNotifications(String token, LocalDateTime dateTime, Long notificationId, int size) {
        UUID memberId = jwtTokenProvider.getMemberIdFromToken(token);
        LocalDateTime createdAt = dateTime != null ? dateTime : LocalDateTime.now();
        Long id = notificationId != null ? notificationId : Long.MAX_VALUE;

        List<Notification> notifications = notificationRepository.findNextUnreadPageByReceiverId(
                memberId, createdAt, id, PageRequest.of(0, size + 1)
        );

        return getNotificationCursorResponse(notifications, size);
    }

    private CursorResponse<NotificationResponse> getNotificationCursorResponse(List<Notification> notifications, int size) {
        boolean hasNext = notifications.size() > size;

        if (hasNext) {
            notifications = notifications.subList(0, size);
        }

        List<NotificationResponse> responseList = notifications.stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());

        LocalDateTime nextCursorCreatedAt = hasNext ? notifications.get(notifications.size() - 1).getCreatedAt() : null;
        Long nextCursorId = hasNext ? notifications.get(notifications.size() - 1).getId() : null;

        return CursorResponse.from(responseList, hasNext, nextCursorCreatedAt, nextCursorId);
    }

}
