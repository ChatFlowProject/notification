package shop.flowchat.notification.domain.mention;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MentionMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mention_id")
    private Mention mention;

    @Builder
    public MentionMember(UUID memberId, Mention mention) {
        this.memberId = memberId;
        this.mention = mention;
    }

    public static MentionMember create(UUID memberId, Mention mention) {
        return builder().memberId(memberId).mention(mention).build();
    }

}
