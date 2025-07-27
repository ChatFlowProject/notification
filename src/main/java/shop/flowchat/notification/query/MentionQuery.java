package shop.flowchat.notification.query;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.common.dto.MentionMessageResponse;
import shop.flowchat.notification.common.dto.ChannelContextDto;
import shop.flowchat.notification.common.dto.MessageInfo;
import shop.flowchat.notification.common.util.JwtTokenProvider;
import shop.flowchat.notification.domain.member.MemberReadModel;
import shop.flowchat.notification.domain.mention.Mention;
import shop.flowchat.notification.domain.mention.MentionMember;
import shop.flowchat.notification.domain.message.MessageReadModel;
import shop.flowchat.notification.infrastructure.repository.channel.ChannelReadModelRepository;
import shop.flowchat.notification.infrastructure.repository.mention.MentionMemberRepository;
import shop.flowchat.notification.infrastructure.repository.message.MessageReadModelRepository;
import shop.flowchat.notification.presentation.dto.CursorResponse;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentionQuery {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberReadModelQuery memberQuery;
    private final MentionMemberRepository mentionMemberRepository;
    private final MessageReadModelRepository messageRepository;
    private final ChannelReadModelRepository channelRepository;

    public CursorResponse<MentionMessageResponse> findMentionsByMemberId(String token, Long nextCursorId, LocalDateTime nextCursorCreatedAt,
                                                                         int size, Boolean includeEveryone, Boolean includeAllTeams, UUID teamId) {
        UUID memberId = jwtTokenProvider.getMemberIdFromToken(token);

        Pageable pageable = PageRequest.of(0, size + 1);

        if (nextCursorId == null || nextCursorCreatedAt == null) {
            List<MentionMember> mentionMembers = mentionMemberRepository.findLatestMentions(memberId, includeEveryone, includeAllTeams, teamId, pageable);
            return buildMentionResponse(mentionMembers, size);
        }

        MentionMember cursorMentionMember = mentionMemberRepository.findById(nextCursorId).orElse(null);
        if (cursorMentionMember == null) {
            List<MentionMember> mentionMembers = mentionMemberRepository.findByCursor(
                    memberId, includeEveryone, includeAllTeams, teamId,
                    nextCursorId, nextCursorCreatedAt, pageable
            );
            return buildMentionResponse(mentionMembers, size);
        }

        List<MentionMember> mentionMembers = mentionMemberRepository.findByCursor(
                memberId, includeEveryone, includeAllTeams, teamId,
                nextCursorId, nextCursorCreatedAt, pageable
        );
        return buildMentionResponse(mentionMembers, size);
    }

    private CursorResponse<MentionMessageResponse> buildMentionResponse(List<MentionMember> mentionMembers, int size) {
        Boolean hasNext = false;
        Long nextCursorId = null;
        LocalDateTime nextCursorCreatedAt = null;

        if (mentionMembers.size() > size) {
            hasNext = true;
            MentionMember lastMentionMember = mentionMembers.get(mentionMembers.size() - 1);
            nextCursorId = lastMentionMember.getId();
            nextCursorCreatedAt = lastMentionMember.getMention().getCreatedAt();
            mentionMembers = mentionMembers.subList(0, size);
        }

        List<Mention> mentions = mentionMembers.stream()
            .map(MentionMember::getMention)
            .toList();

        List<Long> messageIds = mentions.stream()
            .map(Mention::getMessageId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();

        List<MessageReadModel> messages = messageRepository.findAllByIdOrderByCreatedAtDesc(messageIds);

        List<UUID> chatIds = messages.stream()
            .map(MessageReadModel::getChatId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();

        List<ChannelContextDto> channelDtos = channelRepository.findAllChannelCategoryTeamByChatIds(chatIds);
        Map<UUID, ChannelContextDto> channelMap = new HashMap<>();
        for (ChannelContextDto dto : channelDtos) {
            channelMap.put(dto.channel().getChatId(), dto);
        }

        List<MentionMessageResponse> responses = messages.stream()
                .map(message -> toMentionResponse(message, channelMap))
                .filter(Objects::nonNull)
                .toList();

        return CursorResponse.from(responses, hasNext, nextCursorCreatedAt, nextCursorId);
    }

    private MentionMessageResponse toMentionResponse(MessageReadModel message, Map<UUID, ChannelContextDto> channelMap) {
        ChannelContextDto channelContextDto = channelMap.get(message.getChatId());
        MemberReadModel sender = memberQuery.getMemberById(message.getMemberId());
        if (sender == null) return null;
        return MentionMessageResponse.from(sender, channelContextDto, MessageInfo.from(message));
    }
}
