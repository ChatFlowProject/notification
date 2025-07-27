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
public class TeamReadModel {
    @Id
    private UUID id;
    private String name;
    private String iconUrl;

    public void update(String name, String iconUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
    }
}