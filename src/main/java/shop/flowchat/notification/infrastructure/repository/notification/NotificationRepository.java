package shop.flowchat.notification.infrastructure.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shop.flowchat.notification.domain.notification.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT DISTINCT n FROM Notification n JOIN FETCH n.sender WHERE n.receiverId = :receiverId ORDER BY n.createdAt DESC")
    List<Notification> findAllByReceiverIdOrderByCreatedAt(UUID receiverId);

    @Query("SELECT DISTINCT n FROM Notification n JOIN FETCH n.sender WHERE n.receiverId = :receiverId AND n.isRead = :isRead ORDER BY n.createdAt DESC")
    List<Notification> findAllByReceiverIdAndIsReadOrderByCreatedAt(UUID receiverId, Boolean isRead);

    List<Notification> findAllByReceiverIdAndIsRead(UUID receiverId, Boolean isRead);

    void deleteAllByReceiverId(UUID receiverId);

}
