package shop.flowchat.notification.infrastructure.repository.mention;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.notification.domain.mention.MentionMember;

public interface MentionMemberRepository extends JpaRepository<MentionMember, Long> {
    List<MentionMember>findByMentionId(Long id);
    void deleteByMentionIdAndMemberIdIn(Long id, List<UUID> memberIds);
    void deleteByMentionId(Long id);
    List<MentionMember> findAllByMemberId(UUID memberId, Pageable pageable);
}