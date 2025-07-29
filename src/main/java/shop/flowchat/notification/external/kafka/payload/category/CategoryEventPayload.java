package shop.flowchat.notification.external.kafka.payload.category;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryEventPayload(
        Long id,
        String name,
        UUID teamId,
        LocalDateTime timestamp
) {}
