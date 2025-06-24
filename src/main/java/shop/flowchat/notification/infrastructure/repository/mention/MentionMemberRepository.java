package shop.flowchat.notification.infrastructure.repository.mention;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.notification.domain.mention.MentionMember;

public interface MentionMemberRepository extends JpaRepository<MentionMember, Long> {
}