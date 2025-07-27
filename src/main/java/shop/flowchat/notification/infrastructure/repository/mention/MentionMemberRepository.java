package shop.flowchat.notification.infrastructure.repository.mention;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shop.flowchat.notification.domain.mention.MentionMember;

public interface MentionMemberRepository extends JpaRepository<MentionMember, Long> {
    List<MentionMember>findByMentionId(Long id);
    void deleteByMentionIdAndMemberIdIn(Long id, List<UUID> memberIds);
    void deleteByMentionId(Long id);
    @Query("""
        select mm from MentionMember mm
        join fetch mm.mention m where mm.memberId = :memberId
        and (:includeEveryone = true or m.type <> 'EVERYONE') and (:includeAllTeams = true or m.teamId = :teamId)
        order by m.createdAt desc
    """)
    List<MentionMember> findAllWithMentionByFilters(
            UUID memberId,
            boolean includeEveryone,
            boolean includeAllTeams,
            UUID teamId,
            Pageable pageable
    );
}