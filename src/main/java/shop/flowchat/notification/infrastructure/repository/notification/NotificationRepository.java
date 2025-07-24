package shop.flowchat.notification.infrastructure.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.notification.domain.notification.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
