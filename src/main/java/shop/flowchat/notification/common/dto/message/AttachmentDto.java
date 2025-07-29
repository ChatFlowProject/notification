package shop.flowchat.notification.common.dto.message;

public record AttachmentDto(
        AttachmentType type,
        String url
) {}