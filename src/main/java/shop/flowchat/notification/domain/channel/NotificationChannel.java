package shop.flowchat.notification.domain.channel;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import java.util.UUID;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class NotificationChannel {
    @Id
    private Long id;
    private UUID teamId;
    private String name;
    private UUID chatId;
    @ElementCollection
    @CollectionTable(name = "notification_channel_members", joinColumns = @JoinColumn(name = "channel_id"))
    @Column(name = "member_id")
    private List<UUID> memberIds;


    public void update(String name, List<UUID> memberIds) {
        this.name = name;
        this.memberIds = memberIds;
    }
}