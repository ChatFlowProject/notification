package shop.flowchat.notification.domain.channel;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private Long categoryId;
    private UUID chatId;
    @Enumerated(EnumType.STRING)
    private ChannelReadModelAccessType accessType;
}