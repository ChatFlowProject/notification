package shop.flowchat.notification.event.payload;

import java.time.LocalDateTime;
import java.util.UUID;

public record FriendshipEventPayload(
        String id,
        UUID fromMemberId,
        UUID toMemberId,
        LocalDateTime timestamp
) {
}
