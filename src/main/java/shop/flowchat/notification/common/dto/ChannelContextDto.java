package shop.flowchat.notification.common.dto;

import shop.flowchat.notification.domain.category.CategoryReadModel;
import shop.flowchat.notification.domain.channel.ChannelReadModel;
import shop.flowchat.notification.domain.team.TeamReadModel;

public record ChannelContextDto(
    ChannelReadModel channel,
    CategoryReadModel category,
    TeamReadModel team
) {
    public ChannelContextDto(ChannelReadModel channel, CategoryReadModel category, TeamReadModel team) {
        this.channel = channel;
        this.category = category;
        this.team = team;
    }
}