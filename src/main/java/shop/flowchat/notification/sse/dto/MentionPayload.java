package shop.flowchat.notification.sse.dto;

import shop.flowchat.notification.common.dto.*;

import java.time.LocalDateTime;
import shop.flowchat.notification.domain.team.NotificationTeam;

public record MentionPayload(
        MemberInfo sender,
        TeamInfo team,
        ChannelInfo channel,
        Long messageId,
        String content,
        LocalDateTime createdAt
) {
    public static MentionPayload from(MentionSseEvent event) {
        return new MentionPayload(
                event.sender(),
                event.team(),
                event.channel(),
                event.messageId(),
                event.content(),
                event.createdAt()
        );
    }
}