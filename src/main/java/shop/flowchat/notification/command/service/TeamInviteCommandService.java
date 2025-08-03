package shop.flowchat.notification.command.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.flowchat.notification.common.dto.ChannelContextDto;
import shop.flowchat.notification.domain.member.MemberReadModel;
import shop.flowchat.notification.domain.notification.Notification;
import shop.flowchat.notification.domain.notification.NotificationType;
import shop.flowchat.notification.external.kafka.payload.message.TeamInviteEventPayload;
import shop.flowchat.notification.external.sse.dto.TeamInviteSsePayload;
import shop.flowchat.notification.external.sse.repository.SseEmitterRepository;
import shop.flowchat.notification.infrastructure.repository.notification.NotificationRepository;
import shop.flowchat.notification.query.MentionTargetQuery;
import shop.flowchat.notification.query.MemberReadModelQuery;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamInviteCommandService {
    private final NotificationRepository notificationRepository;
    private final SseEmitterRepository emitterRepository;
    private final MemberReadModelQuery memberQuery;
    private final MentionTargetQuery channelQuery;

    public void createTeamInvite(TeamInviteEventPayload payload) {
        MemberReadModel sender = memberQuery.getMemberById(payload.memberId());
        ChannelContextDto contextDto = channelQuery.findJoinedChannelByChatId(payload.chatId());
        List<UUID> memberIds = channelQuery.findChannelMembers(contextDto.channel().getId());
        if (memberIds.size() != 2) {
            throw new IllegalArgumentException("1 대 1 DM 채널이 아닙니다: " + memberIds.size() + "명");
        }
        UUID receiverId = memberIds.get(0).equals(sender.getId()) ? memberIds.get(1) : memberIds.get(0);

        Notification notification = Notification.create(
                sender,
                receiverId,
                NotificationType.TEAM_INVITE,
                String.format("%s 님이 %s 팀으로 초대했어요.", sender.getName(), contextDto.team().getName()),
                payload.chatId(),
                payload.messageId()
        );
        notificationRepository.save(notification);
        sendInviteNotification(TeamInviteSsePayload.from(notification, sender, contextDto), receiverId);
    }

    public void deleteTeamInvite(TeamInviteEventPayload payload) {
        notificationRepository.findByMessageId(payload.messageId())
                .ifPresent(notificationRepository::delete);
    }

    private void sendInviteNotification(TeamInviteSsePayload payload, UUID receiverId) {
            SseEmitter emitter = emitterRepository.get(receiverId);
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("inviteTeam")
                            .data(payload));
                } catch (IOException e) {
                    emitterRepository.remove(receiverId);
                }
            }
    }
}
