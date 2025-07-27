package shop.flowchat.notification.domain.mention;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import shop.flowchat.notification.event.payload.MentionEventPayload;
import shop.flowchat.notification.domain.channel.ChannelReadModel;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Mention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MentionType type;

    @OneToMany(mappedBy = "mention", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MentionMember> members = new ArrayList<>();

    private Long messageId;
    private Long channelId;
    // 조회 성능 향상을 위한 컬럼 (필터링)
    private UUID teamId;

    private LocalDateTime createdAt;

    @Builder
    public Mention(Long messageId, Long channelId, MentionType type, LocalDateTime createdAt) {
        this.messageId = messageId;
        this.channelId = channelId;
        this.type = type;
        this.createdAt = createdAt;
        this.members = new ArrayList<>();
    }

    public static Mention create(MentionEventPayload event, ChannelReadModel channel) {
        return Mention.builder()
                .messageId(event.messageId())
                .channelId(channel.getId())
                .type(event.type())
                .createdAt(event.createdAt())
                .build();
    }
}