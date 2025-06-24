package shop.flowchat.notification.event.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import shop.flowchat.notification.external.dto.team.TeamUpdatedEvent;
import shop.flowchat.notification.query.NotificationTeamQuery;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamEventConsumer {
    private final NotificationTeamQuery teamQuery;

    @KafkaListener(topics = "team-updated", groupId = "notification-group")
    public void consume(TeamUpdatedEvent event) {
        log.info("TeamUpdatedEvent : {}", event);
        teamQuery.update(event);
    }
}