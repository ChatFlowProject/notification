package shop.flowchat.notification.domain.mention;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.flowchat.notification.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MentionMember extends BaseEntity {
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
