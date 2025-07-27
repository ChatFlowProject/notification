package shop.flowchat.notification.infrastructure.repository.notification;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.flowchat.notification.domain.notification.Notification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("""
                SELECT n FROM Notification n
                JOIN FETCH n.sender
                WHERE n.receiverId = :receiverId
                AND (n.createdAt < :createdAt OR (n.createdAt = :createdAt AND n.id < :id))
                ORDER BY n.createdAt DESC, n.id DESC
            """)
    List<Notification> findNextPageByReceiverId(
            @Param("receiverId") UUID receiverId,
            @Param("createdAt") LocalDateTime createdAt,
            @Param("id") Long cursorId,
            Pageable pageable
    );

    @Query("""
                SELECT n FROM Notification n
                JOIN FETCH n.sender
                WHERE n.receiverId = :receiverId
                AND n.isRead = false
                AND (n.createdAt < :createdAt OR (n.createdAt = :createdAt AND n.id < :id))
                ORDER BY n.createdAt DESC, n.id DESC
            """)
    List<Notification> findNextUnreadPageByReceiverId(
            @Param("receiverId") UUID receiverId,
            @Param("createdAt") LocalDateTime createdAt,
            @Param("id") Long id,
            Pageable pageable
    );

    List<Notification> findAllByReceiverIdAndIsRead(UUID receiverId, Boolean isRead);

    void deleteAllByReceiverId(UUID receiverId);

}
