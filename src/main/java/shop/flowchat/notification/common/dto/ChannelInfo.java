package shop.flowchat.notification.common.dto;

import shop.flowchat.notification.domain.channel.NotificationChannel;

public record ChannelInfo(
        Long id,
        String name
) {
    public static ChannelInfo from(NotificationChannel channel) {
        return new ChannelInfo(
                channel.getId(),
                channel.getName()
        );
    }
}
