package shop.flowchat.notification.external.kafka.payload.team;

import java.time.LocalDateTime;
import java.util.UUID;

public record TeamMemberEventPayload(
        Long id,
        UUID teamId,
        UUID memberId,
        LocalDateTime timestamp
) {}
