package shop.flowchat.notification.event.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import shop.flowchat.notification.external.dto.channel.ChannelUpdatedEvent;
import shop.flowchat.notification.external.dto.team.TeamUpdatedEvent;
import shop.flowchat.notification.query.NotificationChannelQuery;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelEventConsumer {
    private final NotificationChannelQuery channelQuery;

    @KafkaListener(topics = "channel", groupId = "notification-group")
    public void consume(ChannelUpdatedEvent event) {
        log.info("ChannelUpdatedEvent : {}", event);
        channelQuery.update(event);
    }
}