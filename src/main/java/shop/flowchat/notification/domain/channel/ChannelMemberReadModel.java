package shop.flowchat.notification.domain.channel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelMemberReadModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long channelId;
    private UUID memberId;

    @Builder
    private ChannelMemberReadModel(Long channelId, UUID memberId) {
        this.channelId = channelId;
        this.memberId = memberId;
    }

    public static ChannelMemberReadModel create(Long channelId, UUID memberId) {
        return ChannelMemberReadModel.builder()
                .channelId(channelId)
                .memberId(memberId)
                .build();
    }
}
