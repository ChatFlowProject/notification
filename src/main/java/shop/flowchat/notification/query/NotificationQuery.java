package shop.flowchat.notification.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.common.util.JwtTokenProvider;
import shop.flowchat.notification.domain.notification.Notification;
import shop.flowchat.notification.infrastructure.repository.notification.NotificationRepository;
import shop.flowchat.notification.presentation.dto.NotificationResponse;

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

    public List<NotificationResponse> getAllNotifications(String token) {
        UUID memberId = jwtTokenProvider.getMemberIdFromToken(token);
        return notificationRepository.findAllByReceiverIdOrderByCreatedAt(memberId).stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
    }

}
