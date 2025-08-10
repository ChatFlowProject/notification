package shop.flowchat.notification.query;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.common.dto.ChannelContextDto;
import shop.flowchat.notification.domain.channel.ChannelMemberReadModel;
import shop.flowchat.notification.domain.channel.ChannelReadModel;
import shop.flowchat.notification.domain.team.TeamReadModel;
import shop.flowchat.notification.infrastructure.repository.channel.ChannelMemberReadModelRepository;
import shop.flowchat.notification.infrastructure.repository.channel.ChannelReadModelRepository;
import shop.flowchat.notification.infrastructure.repository.team.TeamMemberReadModelRepository;
import shop.flowchat.notification.infrastructure.repository.team.TeamReadModelRepository;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentionTargetQuery {
    private final TeamReadModelRepository teamRepository;
    private final TeamMemberReadModelRepository teamMemberRepository;
    private final ChannelReadModelRepository channelRepository;
    private final ChannelMemberReadModelRepository channelMemberRepository;

    public ChannelContextDto findJoinedChannelByChatId(UUID chatId) {
        return channelRepository.findChannelCategoryTeamByChatId(chatId);
    }

    public ChannelReadModel findChannelByChatId(UUID chatId) {
        return channelRepository.findByChatId(chatId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다. : " + chatId));
    }

    public TeamReadModel findTeamByTeamId(UUID id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다. : " + id));
    }

    public List<UUID> findTeamMembers(UUID teamId) {
        return teamMemberRepository.findMemberIdsByTeamId(teamId);
    }
    
    public List<UUID> findChannelMembers(Long channelId) {
        return channelMemberRepository.findByChannelId(channelId).stream()
                .map(ChannelMemberReadModel::getMemberId)
                .toList();
    }
}