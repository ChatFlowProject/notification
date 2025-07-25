package shop.flowchat.notification.common.dto;

import shop.flowchat.notification.domain.channel.ChannelReadModel;

public record ChannelInfo(
        Long id,
        String name
) {
    public static ChannelInfo from(ChannelReadModel channel) {
        return new ChannelInfo(
                channel.getId(),
                channel.getName()
        );
    }
}
