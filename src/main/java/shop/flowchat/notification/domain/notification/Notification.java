package shop.flowchat.notification.domain.notification;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.flowchat.notification.domain.BaseEntity;
import shop.flowchat.notification.domain.member.MemberReadModel;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private MemberReadModel sender;

    @Column(nullable = false)
    private UUID receiverId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(length = 500, nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean isRead;

    private String targetId; // ex. teamId

    @Builder
    private Notification(MemberReadModel sender, UUID receiverId, NotificationType type, String message, Boolean isRead, String targetId) {
        this.sender = sender;
        this.receiverId = receiverId;
        this.type = type;
        this.message = message;
        this.isRead = isRead;
        this.targetId = targetId;
    }

    public static Notification create(MemberReadModel sender, UUID receiverId, NotificationType type, String message, String targetId) {
        return Notification.builder()
                .sender(sender)
                .receiverId(receiverId)
                .type(type)
                .message(message)
                .isRead(false)
                .targetId(targetId)
                .build();
    }

    public void markAsRead() {
        this.isRead = true;
    }

}
