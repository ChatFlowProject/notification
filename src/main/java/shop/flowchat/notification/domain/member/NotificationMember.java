package shop.flowchat.notification.domain.member;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class NotificationMember {
    @Id
    private UUID id;
    private String name;
    private String avatarUrl;

    public void update(String name, String avatarUrl) {
        this.name = name;
        this.avatarUrl = avatarUrl;
    }
}