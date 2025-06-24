package shop.flowchat.notification.query;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.domain.channel.NotificationChannel;
import shop.flowchat.notification.domain.team.NotificationTeam;
import shop.flowchat.notification.external.dto.channel.ChannelUpdatedEvent;
import shop.flowchat.notification.infrastructure.repository.channel.NotificationChannelRepository;
import shop.flowchat.notification.infrastructure.repository.team.NotificationTeamRepository;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationChannelQuery {
    private final NotificationChannelRepository channelRepository;
    private final NotificationTeamRepository teamRepository;

    public Pair<NotificationTeam, NotificationChannel> findChannelByChatId(UUID chatId) {
        NotificationChannel channel = channelRepository.findByChatId(chatId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다: " + chatId));
        NotificationTeam team = teamRepository.findById(channel.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다: " + channel.getTeamId()));

        return Pair.of(team, channel);
    }

    @Transactional
    public void update(ChannelUpdatedEvent event) {
        NotificationChannel channel = channelRepository.findById(event.id())
                .orElseThrow(() -> new IllegalArgumentException("해당 채널을 찾을 수 없습니다 : " + event.id()));
        channel.update(event.name(), event.memberIds());
        channelRepository.save(channel);
    }
}
