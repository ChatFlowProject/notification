package shop.flowchat.notification.external.kafka.payload.message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.flowchat.notification.common.dto.message.AttachmentDto;

public record MentionEventPayload(
        UUID memberId,
        UUID chatId,
        Long messageId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean isUpdated,
        Boolean isDeleted,
        List<AttachmentDto> attachments,
        List<String> memberIds
) {}