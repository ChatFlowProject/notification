package shop.flowchat.notification.event.payload;

import java.time.LocalDateTime;
import java.util.UUID;

public record TeamMemberEventPayload(
        Long id,
        UUID teamId,
        UUID memberId,
        LocalDateTime timestamp
) {}
