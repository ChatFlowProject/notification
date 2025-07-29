package shop.flowchat.notification.domain.category;

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
import shop.flowchat.notification.event.payload.CategoryEventPayload;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryReadModel {
    @Id
    private Long id;
    private String name;
    private UUID teamId;
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    private CategoryReadModel(Long id, String name, UUID teamId, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.teamId = teamId;
        this.createdAt = createdAt;
    }

    public static CategoryReadModel create(CategoryEventPayload payload) {
        return CategoryReadModel.builder()
                .id(payload.id())
                .name(payload.name())
                .teamId(payload.teamId())
                .createdAt(payload.timestamp())
                .build();
    }
}
