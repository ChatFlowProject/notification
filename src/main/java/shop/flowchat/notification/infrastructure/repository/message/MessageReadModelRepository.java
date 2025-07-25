package shop.flowchat.notification.infrastructure.repository.message;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.notification.domain.message.MessageReadModel;

public interface MessageReadModelRepository extends JpaRepository<MessageReadModel, Long> {
}