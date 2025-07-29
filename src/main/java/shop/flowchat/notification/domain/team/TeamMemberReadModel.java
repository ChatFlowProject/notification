package shop.flowchat.notification.domain.team;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.flowchat.notification.event.payload.TeamMemberEventPayload;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMemberReadModel {
    @Id
    private Long id;
    private UUID teamId;
    private UUID memberId;

    @Builder
    private TeamMemberReadModel(Long id, UUID teamId, UUID memberId) {
        this.id = id;
        this.teamId = teamId;
        this.memberId = memberId;
    }
    public static TeamMemberReadModel create(TeamMemberEventPayload payload) {
        return TeamMemberReadModel.builder()
                .id(payload.id())
                .teamId(payload.teamId())
                .memberId(payload.memberId())
                .build();
    }
}
