package shop.flowchat.notification.external.dto.channel;

import java.util.List;
import java.util.UUID;

public record ChannelUpdatedEvent(
        Long id,
        String name,
        List<UUID> memberIds
) {}
