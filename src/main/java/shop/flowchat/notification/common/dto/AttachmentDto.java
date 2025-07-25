package shop.flowchat.notification.common.dto;

public record AttachmentDto(
        AttachmentType type,
        String url
) {}