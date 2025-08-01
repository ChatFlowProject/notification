package shop.flowchat.notification.command.service;

import java.io.IOException;
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
        ChannelContextDto contextDto =channelQuery.findJoinedChannelByChatId(payload.chatId());
        Notification notification = Notification.create(
                sender,
                null, // Todo: receiverId dialog 서버에서 받아오기
                NotificationType.TEAM_INVITE,
                String.format("%s 님이 %s 팀으로 초대했어요.", sender.getName(), contextDto.team().getName()),
                payload.chatId(),
                payload.messageId()
        );
        notificationRepository.save(notification);
        sendInviteNotification(TeamInviteSsePayload.from(notification, sender, contextDto), notification.getReceiverId());
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
