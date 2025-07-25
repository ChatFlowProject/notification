package shop.flowchat.notification.sse.dto;

import java.io.Serializable;
import org.apache.commons.lang3.tuple.Pair;
import shop.flowchat.notification.command.dto.MentionCreateEvent;
import shop.flowchat.notification.common.dto.MemberInfo;
import shop.flowchat.notification.common.dto.TeamInfo;
import shop.flowchat.notification.common.dto.ChannelInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MentionSseEvent(
    List<UUID> receiverIds,
    MemberInfo sender,
    TeamInfo team,
    ChannelInfo channel,
    Long messageId,
    String content,
    LocalDateTime createdAt
) implements Serializable {
    public static MentionSseEvent from(List<UUID> receiverIds, MemberInfo sender, TeamInfo team, ChannelInfo channel , MentionCreateEvent event) {
        return new MentionSseEvent(
                receiverIds,
                sender,
                team,
                channel,
                event.messageId(),
                event.content(),
                event.createdAt()
        );
    }
}
