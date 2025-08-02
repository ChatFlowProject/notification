package shop.flowchat.notification.infrastructure.repository.message;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.flowchat.notification.domain.message.AttachmentReadModel;

public interface AttachmentReadModelRepository extends JpaRepository<AttachmentReadModel, Long> {
    void deleteByMessageId(Long messageId);

    @Modifying
    @Query("DELETE FROM AttachmentReadModel a WHERE a.messageId IN :messageIds")
    void deleteByMessageIds(@Param("messageIds") List<Long> messageIds);
}