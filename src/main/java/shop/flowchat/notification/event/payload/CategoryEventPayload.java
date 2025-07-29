package shop.flowchat.notification.event.payload;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryEventPayload(
        Long id,
        String name,
        UUID teamId,
        LocalDateTime timestamp
) {}
