package shop.flowchat.notification.query;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.domain.channel.ChannelReadModel;
import shop.flowchat.notification.domain.mention.MentionType;
import shop.flowchat.notification.domain.team.TeamReadModel;
import shop.flowchat.notification.event.payload.MentionEventPayload;
import shop.flowchat.notification.infrastructure.repository.channel.ChannelReadModelRepository;
import shop.flowchat.notification.infrastructure.repository.team.TeamMemberReadModelRepository;
import shop.flowchat.notification.infrastructure.repository.team.TeamReadModelRepository;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentionTargetQuery {
    private final TeamMemberReadModelRepository teamMemberRepository;
    private final ChannelReadModelRepository channelRepository;
    private final TeamReadModelRepository teamRepository;

    public ChannelReadModel findChannelByChatId(UUID chatId) {
        return channelRepository.findByChatId(chatId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다: " + chatId));
    }

    public TeamReadModel findTeamById(UUID teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다: " + teamId));
    }

    public List<UUID> resolveMentionTargetMembers(UUID teamId, MentionEventPayload payload) {
        if (payload.type() == MentionType.EVERYONE) {
            return teamMemberRepository.findMemberIdsByTeamId(teamId);
        } else {
            return payload.memberIds();
        }
    }
}
