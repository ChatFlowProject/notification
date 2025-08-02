package shop.flowchat.notification.infrastructure.repository.mention;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.flowchat.notification.domain.mention.Mention;

public interface MentionRepository extends JpaRepository<Mention, Long> {
    Mention findByMessageId(Long messageId);

    @Query("select m.id from Mention m where m.messageId in :messageIds")
    List<Long> findIdsByMessageIdIn(@Param("messageIds") List<Long> messageIds);

    @Modifying(clearAutomatically = true)
    @Query("delete from Mention m where m.id in :mentionIds")
    void deleteByIdIn(@Param("mentionIds") List<Long> mentionIds);
}