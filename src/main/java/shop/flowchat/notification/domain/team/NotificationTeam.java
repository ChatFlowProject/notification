package shop.flowchat.notification.domain.team;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class NotificationTeam {
    @Id
    private UUID id;
    private String name;
    private String imageUrl;

    public void update(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }
}