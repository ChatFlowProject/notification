package shop.flowchat.notification.infrastructure.repository.team;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.flowchat.notification.domain.team.TeamMemberReadModel;

public interface TeamMemberReadModelRepository extends JpaRepository<TeamMemberReadModel, Long> {
    @Query("SELECT tm.memberId FROM TeamMemberReadModel tm WHERE tm.teamId = :teamId")
    List<UUID> findMemberIdsByTeamId(@Param("teamId") UUID teamId);

    void deleteByTeamId(UUID teamId);

    Boolean existsByTeamIdAndMemberId(UUID teamId, UUID memberId);

    TeamMemberReadModel findByTeamIdAndMemberId(UUID teamId, UUID memberId);
}