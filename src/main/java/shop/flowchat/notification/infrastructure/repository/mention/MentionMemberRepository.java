package shop.flowchat.notification.infrastructure.repository.mention;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.flowchat.notification.domain.mention.MentionMember;

public interface MentionMemberRepository extends JpaRepository<MentionMember, Long> {
    List<MentionMember>findByMentionId(Long id);

    void deleteByMentionIdAndMemberIdIn(Long id, List<UUID> memberIds);

    void deleteByMentionId(Long id);

    @Query("""
        SELECT mm FROM MentionMember mm
        JOIN FETCH mm.mention m WHERE mm.memberId = :memberId
        AND (:includeEveryone = true OR m.type <> 'EVERYONE') AND (:includeAllTeams = true OR m.teamId = :teamId)
        ORDER BY m.createdAt DESC, mm.id DESC
    """)
    List<MentionMember> findLatestMentions(
            UUID memberId,
            Boolean includeEveryone,
            Boolean includeAllTeams,
            UUID teamId,
            Pageable pageable
    );

    @Query("""
        SELECT mm FROM MentionMember mm
        JOIN FETCH mm.mention m WHERE mm.memberId = :memberId
        AND (:includeEveryone = true OR m.type <> 'EVERYONE') AND (:includeAllTeams = true OR m.teamId = :teamId)
        AND (
           m.createdAt < :createdAt
            OR (m.createdAt = :createdAt AND mm.id <= :id)
        )
        ORDER BY m.createdAt DESC, mm.id DESC
    """)
    List<MentionMember> findByCursor(
            UUID memberId, Boolean includeEveryone, Boolean includeAllTeams,
            UUID teamId, Long id, LocalDateTime createdAt, Pageable pageable
    );

    @Modifying
    @Query("delete from MentionMember mm where mm.mention.id in :mentionIds")
    void deleteByMentionIdIn(@Param("mentionIds") List<Long> mentionIds);
}