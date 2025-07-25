package shop.flowchat.notification.infrastructure.repository.message;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.notification.domain.message.AttachmentReadModel;

public interface AttachmentReadModelRepository extends JpaRepository<AttachmentReadModel, Long> {
    void deleteByMessageId(Long messageId);
}