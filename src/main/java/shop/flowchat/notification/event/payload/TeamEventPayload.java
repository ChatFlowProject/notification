package shop.flowchat.notification.event.payload;

import java.time.LocalDateTime;
import java.util.UUID;

public record TeamEventPayload(
        UUID id,
        String name,
        String iconUrl,
        LocalDateTime timestamp
) {}
