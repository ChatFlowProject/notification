package shop.flowchat.notification.external.kafka.payload.team;

import java.time.LocalDateTime;
import java.util.UUID;

public record TeamEventPayload(
        UUID id,
        String name,
        String iconUrl,
        LocalDateTime timestamp
) {}
