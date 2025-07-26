package shop.flowchat.notification.query;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import shop.flowchat.notification.common.dto.MemberInfo;
import shop.flowchat.notification.common.util.JwtTokenProvider;
import shop.flowchat.notification.domain.category.CategoryReadModel;
import shop.flowchat.notification.domain.channel.ChannelReadModel;
import shop.flowchat.notification.domain.channel.ChannelReadModelAccessType;
import shop.flowchat.notification.domain.mention.Mention;
import shop.flowchat.notification.domain.message.MessageReadModel;
import shop.flowchat.notification.domain.team.TeamReadModel;
import shop.flowchat.notification.infrastructure.repository.mention.MentionMemberRepository;
import shop.flowchat.notification.infrastructure.repository.message.MessageReadModelRepository;
import shop.flowchat.notification.presentation.dto.MentionMessageResponse;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentionQuery {
    private final JwtTokenProvider jwtTokenProvider;
    private final MentionTargetQuery mentionTargetQuery;
    private final MemberReadModelQuery memberQuery;
    private final MentionMemberRepository mentionMemberRepository;
    private final MessageReadModelRepository messageRepository;

    public List<MentionMessageResponse> findMentionsByMemberId(String token, int page,  int pageSize) {
        UUID memberId = jwtTokenProvider.getMemberIdFromToken(token);
        MemberInfo sender = memberQuery.getMemberInfoById(memberId);

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Direction.DESC, "createdAt"));
        return mentionMemberRepository.findAllByMemberId(memberId, pageable).stream()
            .map(mentionMember -> mapToResponse(mentionMember.getMention(), sender))
            .filter(Objects::nonNull)
            .toList();
    }

    // Todo : 쿼리 날리는 갯수 줄이기 -> in-query? 활용
    private MentionMessageResponse mapToResponse(Mention mention, MemberInfo sender) {
        MessageReadModel message = messageRepository.findById(mention.getMessageId()).orElse(null);
        if (message == null) return null;

        UUID chatId = message.getChatId();
        ChannelReadModel channel = mentionTargetQuery.findChannelByChatId(chatId);
        if (channel == null) return null;

        CategoryReadModel category = null;
        TeamReadModel team = null;

        if (channel.getAccessType() != ChannelReadModelAccessType.PRIVATE) {
            Long categoryId = channel.getCategoryId();
            category = mentionTargetQuery.findCategoryById(categoryId);
            if (category == null) return null;

            UUID teamId = category.getTeamId();
            team = mentionTargetQuery.findTeamById(teamId);
            if (team == null) return null;
        }

        return MentionMessageResponse.from(sender, team, category, channel, message);
    }
}
