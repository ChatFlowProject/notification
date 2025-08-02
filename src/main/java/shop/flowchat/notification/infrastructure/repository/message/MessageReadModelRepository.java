package shop.flowchat.notification.infrastructure.repository.message;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shop.flowchat.notification.domain.message.MessageReadModel;

public interface MessageReadModelRepository extends JpaRepository<MessageReadModel, Long> {
    @Query("""
        SELECT m FROM MessageReadModel m
        WHERE m.id IN :messageIds
        ORDER BY m.createdAt DESC, m.id DESC
    """)
    List<MessageReadModel> findAllByIdOrderByCreatedAtDesc(List<Long> messageIds);

    void deleteByChatId(UUID chatId);

    List<Long> findIdsByChatId(UUID chatId);
}