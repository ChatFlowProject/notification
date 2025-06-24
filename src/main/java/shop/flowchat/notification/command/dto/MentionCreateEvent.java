package shop.flowchat.notification.command.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.flowchat.notification.domain.mention.MentionType;

public record MentionCreateEvent(
        UUID senderId,
        UUID chatId,
        Long messageId,
        String content,
        MentionType type,
        List<UUID> memberIds,
        LocalDateTime createdAt
) {}