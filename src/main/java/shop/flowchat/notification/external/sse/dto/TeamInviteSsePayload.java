package shop.flowchat.notification.external.sse.dto;

import java.time.LocalDateTime;
import shop.flowchat.notification.common.dto.ChannelContextDto;
import shop.flowchat.notification.common.dto.info.MemberInfo;
import shop.flowchat.notification.common.dto.info.TeamInfo;
import shop.flowchat.notification.common.dto.info.DmInfo;
import shop.flowchat.notification.domain.member.MemberReadModel;
import shop.flowchat.notification.domain.notification.Notification;
import shop.flowchat.notification.external.kafka.payload.message.TeamInviteEventPayload;

public record TeamInviteSsePayload(
    MemberInfo sender,
    DmInfo dm,
    TeamInfo team,
    LocalDateTime createdAt
) {
    public static TeamInviteSsePayload from(Notification notification, MemberReadModel sender, ChannelContextDto contextDto) {
        return new TeamInviteSsePayload(
                MemberInfo.from(sender),
                DmInfo.from(notification),
                TeamInfo.from(contextDto.team()),
                notification.getCreatedAt()
        );
    }
}
