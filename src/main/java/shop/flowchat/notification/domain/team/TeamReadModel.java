package shop.flowchat.notification.domain.team;

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
import shop.flowchat.notification.event.payload.TeamEventPayload;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamReadModel {
    @Id
    private UUID id;
    private String name;
    private String iconUrl;
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public TeamReadModel(UUID id, String name, String iconUrl, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
        this.createdAt = createdAt;
    }

    public static TeamReadModel create(TeamEventPayload payload) {
        return TeamReadModel.builder()
                .id(payload.id())
                .name(payload.name())
                .iconUrl(payload.iconUrl())
                .createdAt(payload.timestamp())
                .build();
    }

    public boolean isUpdated(LocalDateTime timestamp) {
        return this.updatedAt == null || timestamp.isAfter(updatedAt);
    }

    public void update(TeamEventPayload payload) {
        this.name = payload.name();
        this.iconUrl = payload.iconUrl();
    }
}