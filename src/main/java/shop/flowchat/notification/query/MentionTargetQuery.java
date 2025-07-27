package shop.flowchat.notification.query;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.common.dto.ChannelContextDto;
import shop.flowchat.notification.infrastructure.repository.channel.ChannelReadModelRepository;
import shop.flowchat.notification.infrastructure.repository.team.TeamMemberReadModelRepository;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentionTargetQuery {
    private final TeamMemberReadModelRepository teamMemberRepository;
    private final ChannelReadModelRepository channelRepository;

    public ChannelContextDto findJoinedChannelByChatId(UUID chatId) {
        return channelRepository.findChannelCategoryTeamByChatId(chatId);
    }

    public List<UUID> findTeamMembers(UUID teamId) {
        return teamMemberRepository.findMemberIdsByTeamId(teamId);
    }
}