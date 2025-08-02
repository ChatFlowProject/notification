package shop.flowchat.notification.external.kafka.consumer.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import shop.flowchat.notification.command.service.TeamInviteCommandService;
import shop.flowchat.notification.external.kafka.payload.message.TeamInviteEventPayload;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamInviteEventConsumer {
    private final ObjectMapper objectMapper;
    private final TeamInviteCommandService commandService;

    @KafkaListener(topics = "teamInvite")
    public void consume(ConsumerRecord<String, String> record, @Header(name = "eventType", required = false) String eventType) {
        try {
            TeamInviteEventPayload payload = objectMapper.readValue(record.value(), TeamInviteEventPayload.class);

            if (eventType == null) {
                log.warn("eventType Header is null. Skipping record: {}", record);
                return;
            }
            switch (eventType) {
                case "teamInviteCreate" -> commandService.createTeamInvite(payload);
                case "teamInviteDelete" -> commandService.deleteTeamInvite(payload);
                default -> log.warn("Unknown team invite eventType: {} Skipping record: {}", eventType, record);
            }
        } catch (Exception e) {
            log.error("Failed to consume event", e);
        }
    }
}
