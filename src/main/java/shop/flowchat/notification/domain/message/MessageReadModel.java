package shop.flowchat.notification.domain.message;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import shop.flowchat.notification.event.payload.MentionEventPayload;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MessageReadModel{
    @Id
    private Long id;
    private UUID chatId;
    private UUID memberId;
    private String content;
    private Boolean isUpdated;
    private Boolean isDeleted;
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    private MessageReadModel(Long id, UUID chatId, UUID memberId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.chatId = chatId;
        this.memberId = memberId;
        this.content = content;
        this.createdAt = createdAt;
        this.isUpdated = false;
        this.isDeleted = false;
    }

    public static MessageReadModel create(MentionEventPayload payload) {
        return MessageReadModel.builder()
                .id(payload.messageId())
                .chatId(payload.chatId())
                .memberId(payload.memberId())
                .content(payload.content())
                .createdAt(payload.createdAt())
                .build();
    }

    public boolean needsUpdate(LocalDateTime timestamp) {
        return updatedAt == null || timestamp.isBefore(updatedAt);
    }

    public void updateContent(String newContent) {
        this.content = newContent;
        this.isUpdated = true;
    }
}