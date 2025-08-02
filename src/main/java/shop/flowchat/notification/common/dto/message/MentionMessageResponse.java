package shop.flowchat.notification.common.dto.message;

import shop.flowchat.notification.common.dto.info.CategoryInfo;
import shop.flowchat.notification.common.dto.info.MemberInfo;
import shop.flowchat.notification.common.dto.info.TeamInfo;
import shop.flowchat.notification.common.dto.ChannelContextDto;
import shop.flowchat.notification.common.dto.info.ChannelInfo;
import shop.flowchat.notification.common.dto.info.MessageInfo;
import shop.flowchat.notification.domain.member.MemberReadModel;

public record MentionMessageResponse(
    MemberInfo sender,
    TeamInfo team,
    CategoryInfo category,
    ChannelInfo channel,
    MessageInfo message
) {
    public static MentionMessageResponse from(MemberReadModel sender, ChannelContextDto channelContextDto, MessageInfo message) {
        return new MentionMessageResponse(
                MemberInfo.from(sender),
                channelContextDto.team() != null ? TeamInfo.from(channelContextDto.team()) : null,
                channelContextDto.category() != null ? CategoryInfo.from(channelContextDto.category()) : null,
                ChannelInfo.from(channelContextDto.channel()),
                message
        );
    }
}
