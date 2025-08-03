package shop.flowchat.notification.external.kafka.payload.channel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.flowchat.notification.domain.channel.ChannelReadModelAccessType;

public record ChannelEventPayload(
        Long id,
        String name,
        ChannelReadModelAccessType channelAccessType,
        Long categoryId,
        UUID chatId,
        List<UUID> channelMembers,
        LocalDateTime timestamp
) {}
