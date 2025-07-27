package shop.flowchat.notification.common.dto;

import java.time.LocalDateTime;
import shop.flowchat.notification.domain.member.MemberReadModel;
import shop.flowchat.notification.domain.message.MessageReadModel;
import shop.flowchat.notification.event.payload.MentionEventPayload;

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
