package shop.flowchat.notification.external.kafka.payload.message;

import java.time.LocalDateTime;
import java.util.UUID;

public record TeamInviteEventPayload(
        UUID memberId,
        UUID chatId,
        Long messageId,
        String content,
        UUID invitedTeamId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
