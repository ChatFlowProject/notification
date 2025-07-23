package shop.flowchat.notification.infrastructure.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.notification.domain.member.MemberReadModel;

import java.util.UUID;

public interface MemberReadModelRepository extends JpaRepository<MemberReadModel, UUID> {

}
