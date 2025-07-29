package shop.flowchat.notification.domain.mention;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import shop.flowchat.notification.external.kafka.payload.message.MentionEventPayload;

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
    private UUID teamId;

    private LocalDateTime createdAt;

    @Builder
    private Mention(Long messageId, MentionType type, UUID teamId, LocalDateTime createdAt) {
        this.messageId = messageId;
        this.type = type;
        this.teamId = teamId;
        this.createdAt = createdAt;
    }

    public static Mention create(MentionEventPayload event, UUID teamId, MentionType type) {
        return Mention.builder()
                .messageId(event.messageId())
                .type(type)
                .teamId(teamId)
                .createdAt(event.createdAt())
                .build();
    }
}