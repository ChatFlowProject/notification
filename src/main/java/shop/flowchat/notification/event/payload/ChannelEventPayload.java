package shop.flowchat.notification.event.payload;

import java.time.LocalDateTime;
import java.util.UUID;
import shop.flowchat.notification.domain.channel.ChannelReadModelAccessType;

public record ChannelEventPayload(
        Long id,
        String name,
        ChannelReadModelAccessType channelAccessType,
        Long categoryId,
        UUID chatId,
        LocalDateTime timestamp
) {}
