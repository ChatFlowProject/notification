package shop.flowchat.notification.external.dto.team;

import java.util.UUID;

public record TeamUpdatedEvent(
        UUID id,
        String name,
        String iconUrl
) {}