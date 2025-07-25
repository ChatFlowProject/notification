package shop.flowchat.notification.domain.channel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelReadModel {
    @Id
    private Long id;
    private String name;
    private UUID teamId;
    private UUID chatId;
    private ChannelReadModelAccessType accessType;
}