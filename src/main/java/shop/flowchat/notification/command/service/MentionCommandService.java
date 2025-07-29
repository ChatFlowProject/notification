package shop.flowchat.notification.command.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.flowchat.notification.common.dto.MentionMessageResponse;
import shop.flowchat.notification.common.dto.ChannelContextDto;
import shop.flowchat.notification.common.dto.MessageInfo;
import shop.flowchat.notification.domain.member.MemberReadModel;
import shop.flowchat.notification.domain.mention.MentionType;
import shop.flowchat.notification.event.payload.MentionEventPayload;
import shop.flowchat.notification.domain.mention.Mention;
import shop.flowchat.notification.domain.mention.MentionMember;
import shop.flowchat.notification.infrastructure.repository.mention.MentionMemberRepository;
import shop.flowchat.notification.infrastructure.repository.mention.MentionRepository;
import shop.flowchat.notification.query.MemberReadModelQuery;
import shop.flowchat.notification.query.MentionTargetQuery;
import shop.flowchat.notification.sse.service.MentionSseService;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MentionCommandService {
    private final MentionRepository mentionRepository;
    private final MentionMemberRepository mentionMemberRepository;
    private final MemberReadModelQuery memberQuery;
    private final MentionTargetQuery mentionTargetQuery;
    private final MentionSseService mentionSseService;

    public void createMention(MentionEventPayload payload) {
        ChannelContextDto channelContextDto = mentionTargetQuery.findJoinedChannelByChatId(payload.chatId());

        MentionType mentionType;
        List<UUID> memberIds;
        if (payload.memberIds() != null && payload.memberIds().size() == 1 && "everyone".equalsIgnoreCase(
                payload.memberIds().get(0))) {
            mentionType = MentionType.EVERYONE;
            memberIds = mentionTargetQuery.findTeamMembers(channelContextDto.team().getId());
        } else if (payload.memberIds() != null && !payload.memberIds().isEmpty()) {
            mentionType = MentionType.INDIVIDUAL;
            memberIds = payload.memberIds().stream().map(UUID::fromString).toList();
        } else {
            mentionType = null;
            memberIds = null;
        }

        Mention mention = Mention.create(payload, channelContextDto.team().getId(), mentionType);
        mentionRepository.save(mention);

        List<MentionMember> mentionMembers = memberIds.stream()
                .map(memberId -> MentionMember.create(memberId, mention))
                .toList();
        mentionMemberRepository.saveAll(mentionMembers);

        MemberReadModel sender = memberQuery.getMemberById(payload.memberId());

        MentionMessageResponse response = MentionMessageResponse.from(sender, channelContextDto, MessageInfo.from(payload));
        mentionSseService.send(memberIds, response);
    }

    public void updateMention(MentionEventPayload payload) {
        Mention mention = mentionRepository.findByMessageId(payload.messageId());
        if (mention == null) {
            createMention(payload);
            return;
        }

        ChannelContextDto channelContextDto = mentionTargetQuery.findJoinedChannelByChatId(payload.chatId());

        List<UUID> oldMemberIds = mentionMemberRepository.findByMentionId(mention.getId())
                .stream()
                .map(MentionMember::getMemberId)
                .toList();

        List<UUID> newMemberIds = mentionTargetQuery.findTeamMembers(channelContextDto.team().getId());

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
            MemberReadModel sender = memberQuery.getMemberById(payload.memberId());

            MentionMessageResponse response = MentionMessageResponse.from(sender, channelContextDto, MessageInfo.from(payload));
            mentionSseService.send(toAdd, response);
        }
    }

    public void deleteMention(MentionEventPayload payload) {
        Mention mention = mentionRepository.findByMessageId(payload.messageId());
        if (mention == null) return;

        mentionMemberRepository.deleteByMentionId(mention.getId());
        mentionRepository.delete(mention);
    }
}