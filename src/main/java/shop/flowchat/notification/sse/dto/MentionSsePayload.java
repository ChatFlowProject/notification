package shop.flowchat.notification.sse.dto;

import shop.flowchat.notification.common.dto.*;

import java.time.LocalDateTime;

public record MentionSsePayload(
        MemberInfo sender,
        TeamInfo team,
        ChannelInfo channel,
        Long messageId,
        String content,
        LocalDateTime createdAt
) {
    public static MentionSsePayload from(MentionSseEvent event) {
        return new MentionSsePayload(
                event.sender(),
                event.team(),
                event.channel(),
                event.messageId(),
                event.content(),
                event.createdAt()
        );
    }
}