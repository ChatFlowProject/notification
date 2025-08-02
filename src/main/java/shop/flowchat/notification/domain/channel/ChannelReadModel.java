package shop.flowchat.notification.domain.channel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import shop.flowchat.notification.external.kafka.payload.channel.ChannelEventPayload;

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
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    private ChannelReadModel(Long id, String name, Long categoryId, UUID chatId, ChannelReadModelAccessType accessType, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.chatId = chatId;
        this.accessType = accessType;
        this.createdAt = createdAt;
    }

    public static ChannelReadModel create(ChannelEventPayload payload) {
        return ChannelReadModel.builder()
                .id(payload.id())
                .name(payload.name())
                .categoryId(payload.categoryId())
                .chatId(payload.chatId())
                .accessType(payload.channelAccessType())
                .createdAt(payload.timestamp())
                .build();
    }


    public boolean needsUpdate(LocalDateTime updatedAt) {
        return this.updatedAt == null || this.updatedAt.isBefore(updatedAt);
    }

    public void update(ChannelEventPayload payload) {
        this.name = payload.name();
        this.categoryId = payload.categoryId();
    }
}