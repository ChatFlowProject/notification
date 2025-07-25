package shop.flowchat.notification.domain.message;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.flowchat.notification.common.dto.AttachmentDto;
import shop.flowchat.notification.common.dto.AttachmentType;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class AttachmentReadModel {
    @Id
    private Long id;
    private Long messageId;
    private String url;
    @Enumerated(EnumType.STRING)
    private AttachmentType type;

    @Builder
    public AttachmentReadModel(Long messageId, String url, AttachmentType type) {
        this.messageId = messageId;
        this.url = url;
        this.type = type;
    }

    public static AttachmentReadModel create(Long messageId, AttachmentDto attachmentDto) {
        return AttachmentReadModel.builder()
                .messageId(messageId)
                .url(attachmentDto.url())
                .type(attachmentDto.type())
                .build();
    }
}
