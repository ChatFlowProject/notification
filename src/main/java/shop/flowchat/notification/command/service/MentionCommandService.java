package shop.flowchat.notification.command.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.flowchat.notification.domain.category.CategoryReadModel;
import shop.flowchat.notification.event.payload.MentionEventPayload;
import shop.flowchat.notification.common.dto.ChannelInfo;
import shop.flowchat.notification.common.dto.MemberInfo;
import shop.flowchat.notification.common.dto.TeamInfo;
import shop.flowchat.notification.domain.channel.ChannelReadModel;
import shop.flowchat.notification.domain.mention.Mention;
import shop.flowchat.notification.domain.mention.MentionMember;
import shop.flowchat.notification.domain.team.TeamReadModel;
import shop.flowchat.notification.infrastructure.repository.mention.MentionMemberRepository;
import shop.flowchat.notification.infrastructure.repository.mention.MentionRepository;
import shop.flowchat.notification.query.MemberReadModelQuery;
import shop.flowchat.notification.query.MentionTargetQuery;
import shop.flowchat.notification.sse.dto.MentionSseEvent;
import shop.flowchat.notification.sse.service.MentionSseService;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MentionCommandService {
    private final MentionRepository mentionRepository;
    private final MentionMemberRepository mentionMemberRepository;
    private final MemberReadModelQuery memberReadModelQuery;
    private final MentionTargetQuery mentionTargetQuery;
    private final MentionSseService mentionSseService;

    public void createMention(MentionEventPayload payload) {
        ChannelReadModel channel = mentionTargetQuery.findChannelByChatId(payload.chatId());
        CategoryReadModel category = mentionTargetQuery.findCategoryById(channel.getCategoryId());
        TeamReadModel team = mentionTargetQuery.findTeamById(category.getTeamId());

        List<UUID> memberIds = mentionTargetQuery.resolveMentionTargetMembers(team.getId(), payload);

        Mention mention = Mention.create(payload, channel);
        mentionRepository.save(mention);

        List<MentionMember> mentionMembers = memberIds.stream()
                .map(memberId -> MentionMember.create(memberId, mention))
                .toList();
        mentionMemberRepository.saveAll(mentionMembers);

        MemberInfo sender = memberReadModelQuery.getMemberInfoById(payload.memberId());

        MentionSseEvent sseEvent = MentionSseEvent.from(
                memberIds,
                sender,
                TeamInfo.from(team),
                ChannelInfo.from(channel),
                payload
        );
        mentionSseService.send(sseEvent);
    }

    public void updateMention(MentionEventPayload payload) {
        Mention mention = mentionRepository.findByMessageId(payload.messageId());
        if (mention == null) {
            createMention(payload);
            return;
        }

        ChannelReadModel channel = mentionTargetQuery.findChannelByChatId(payload.chatId());
        CategoryReadModel category = mentionTargetQuery.findCategoryById(channel.getCategoryId());
        TeamReadModel team = mentionTargetQuery.findTeamById(category.getTeamId());

        List<UUID> oldMemberIds = mentionMemberRepository.findByMentionId(mention.getId())
                .stream()
                .map(MentionMember::getMemberId)
                .toList();

        List<UUID> newMemberIds = mentionTargetQuery.resolveMentionTargetMembers(team.getId(), payload);

        List<UUID> toAdd = newMemberIds.stream()
                .filter(id -> !oldMemberIds.contains(id))
                .toList();

        List<UUID> toRemove = oldMemberIds.stream()
                .filter(id -> !newMemberIds.contains(id))
                .toList();

        if (!toRemove.isEmpty()) {
            mentionMemberRepository.deleteByMentionIdAndMemberIdIn(mention.getId(), toRemove);
        }

        List<MentionMember> addedMentionMembers = toAdd.stream()
                .map(memberId -> MentionMember.create(memberId, mention))
                .toList();
        mentionMemberRepository.saveAll(addedMentionMembers);

        if (!toAdd.isEmpty()) {
            MemberInfo sender = memberReadModelQuery.getMemberInfoById(payload.memberId());

            MentionSseEvent sseEvent = MentionSseEvent.from(
                    toAdd,
                    sender,
                    TeamInfo.from(team),
                    ChannelInfo.from(channel),
                    payload
            );
            mentionSseService.send(sseEvent);
        }
    }

    public void deleteMention(MentionEventPayload payload) {
        Long mentionId = mentionRepository.deleteByMessageId(payload.messageId());
        mentionMemberRepository.deleteByMentionId(mentionId);
    }
}