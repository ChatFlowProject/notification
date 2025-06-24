package shop.flowchat.notification.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.domain.team.NotificationTeam;
import shop.flowchat.notification.external.dto.team.TeamUpdatedEvent;
import shop.flowchat.notification.infrastructure.repository.team.NotificationTeamRepository;

@Component
@RequiredArgsConstructor
public class NotificationTeamQuery {
    private final NotificationTeamRepository teamRepository;

    @Transactional
    public void update(TeamUpdatedEvent event) {
        NotificationTeam team = teamRepository.findById(event.id())
            .orElseThrow(() -> new IllegalArgumentException("해당 팀을 찾을 수 없습니다 : " + event.id()));
        team.update(event.name(), event.iconUrl());
        teamRepository.save(team);
    }
}
