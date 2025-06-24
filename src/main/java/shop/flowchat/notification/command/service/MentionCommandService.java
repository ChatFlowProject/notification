package shop.flowchat.notification.command.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import shop.flowchat.notification.command.dto.MentionCreateEvent;
import shop.flowchat.notification.common.dto.MemberInfo;
import shop.flowchat.notification.domain.channel.NotificationChannel;
import shop.flowchat.notification.domain.mention.Mention;
import shop.flowchat.notification.domain.mention.MentionMember;
import shop.flowchat.notification.domain.team.NotificationTeam;
import shop.flowchat.notification.infrastructure.repository.mention.MentionMemberRepository;
import shop.flowchat.notification.infrastructure.repository.mention.MentionRepository;
import shop.flowchat.notification.domain.mention.MentionType;
import shop.flowchat.notification.query.NotificationChannelQuery;
import shop.flowchat.notification.query.NotificationMemberQuery;
import shop.flowchat.notification.sse.service.MentionSseService;
import shop.flowchat.notification.sse.dto.MentionSseEvent;
import shop.flowchat.notification.common.dto.TeamInfo;
import shop.flowchat.notification.common.dto.ChannelInfo;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MentionCommandService {
    private final MentionRepository mentionRepository;
    private final MentionMemberRepository mentionMemberRepository;
    private final NotificationMemberQuery memberQuery;
    private final NotificationChannelQuery channelQuery;
    private final MentionSseService mentionSseService;

    public void createMention(MentionCreateEvent event) {
        Pair<NotificationTeam, NotificationChannel> pair = channelQuery.findChannelByChatId(event.chatId());
        NotificationTeam teamEntity = pair.getLeft();
        NotificationChannel channelEntity = pair.getRight();

        List<UUID> memberIds = (event.type() == MentionType.EVERYONE)
                ? channelEntity.getMemberIds()
                : event.memberIds();

        Mention mention = Mention.create(event, channelEntity);
        mentionRepository.save(mention);

        List<MentionMember> mentionMembers = memberIds.stream()
                .map(memberId -> MentionMember.create(memberId, mention))
                .toList();
        mentionMemberRepository.saveAll(mentionMembers);

        MemberInfo sender = memberQuery.findById(event.senderId());

        MentionSseEvent sseEvent = MentionSseEvent.from(
            memberIds,
            sender,
            TeamInfo.from(teamEntity),
            ChannelInfo.from(channelEntity),
            event
        );
        mentionSseService.send(sseEvent);
    }
}