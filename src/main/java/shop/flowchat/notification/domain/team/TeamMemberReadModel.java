package shop.flowchat.notification.domain.team;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMemberReadModel {
    @Id
    private Long id;
    private UUID teamId;
    private UUID memberId;
}
