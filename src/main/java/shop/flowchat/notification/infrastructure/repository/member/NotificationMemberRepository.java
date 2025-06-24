package shop.flowchat.notification.infrastructure.repository.member;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.notification.domain.member.NotificationMember;

public interface NotificationMemberRepository extends JpaRepository<NotificationMember, UUID> {
}