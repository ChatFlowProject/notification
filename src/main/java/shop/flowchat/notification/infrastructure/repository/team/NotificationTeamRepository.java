package shop.flowchat.notification.infrastructure.repository.team;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.notification.domain.team.NotificationTeam;

public interface NotificationTeamRepository extends JpaRepository<NotificationTeam, UUID> {
  }