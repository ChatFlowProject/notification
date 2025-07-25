package shop.flowchat.notification.infrastructure.repository.mention;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.notification.domain.mention.Mention;

public interface MentionRepository extends JpaRepository<Mention, Long> {
    Mention findByMessageId(Long messageId);
    Long deleteByMessageId(Long messageId);

}