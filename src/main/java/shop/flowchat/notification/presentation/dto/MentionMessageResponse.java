package shop.flowchat.notification.presentation.dto;

import java.time.LocalDateTime;
import shop.flowchat.notification.common.dto.CategoryInfo;
import shop.flowchat.notification.common.dto.ChannelInfo;
import shop.flowchat.notification.common.dto.MemberInfo;
import shop.flowchat.notification.common.dto.TeamInfo;
import shop.flowchat.notification.domain.category.CategoryReadModel;
import shop.flowchat.notification.domain.channel.ChannelReadModel;
import shop.flowchat.notification.domain.message.MessageReadModel;
import shop.flowchat.notification.domain.team.TeamReadModel;

public record MentionMessageResponse(
        MemberInfo sender,
        TeamInfo team,
        CategoryInfo category,
        ChannelInfo channel,
        Long messageId,
        String content,
        LocalDateTime createdAt
) {
    public static MentionMessageResponse from(MemberInfo sender, TeamReadModel team,
                                              CategoryReadModel category, ChannelReadModel channel, MessageReadModel message) {
        return new MentionMessageResponse(
                sender,
                team != null ? TeamInfo.from(team) : null,
                category != null ? CategoryInfo.from(category) : null,
                ChannelInfo.from(channel),
                message.getId(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}