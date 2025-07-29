package shop.flowchat.notification.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import shop.flowchat.notification.command.service.TeamReadModelCommandService;
import shop.flowchat.notification.event.payload.TeamInitializationPayload;
import shop.flowchat.notification.event.payload.TeamEventPayload;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamEventConsumer {
    private final ObjectMapper objectMapper;
    private final TeamReadModelCommandService commandService;

    @KafkaListener(topics = "team")
    public void consume(ConsumerRecord<String, String> record, @Header(name = "eventType", required = false) String eventType) {
        try {
            if (eventType == null) {
                log.warn("eventType Header is null. Skipping record: {}", record);
                return;
            }
            switch (eventType) {
                case "teamCreate" -> {
                    TeamInitializationPayload payload = objectMapper.readValue(record.value(), TeamInitializationPayload.class);
                    commandService.createTeam(payload);
                }
                case "teamUpdate" -> {
                    TeamEventPayload payload = objectMapper.readValue(record.value(), TeamEventPayload.class);
                    commandService.updateTeam(payload);
                }
                case "teamDelete" -> {
                    TeamEventPayload payload = objectMapper.readValue(record.value(), TeamEventPayload.class);
                    commandService.deleteTeam(payload);
                }
                default -> log.warn("Unknown team eventType: {} Skipping record: {}", eventType, record);
            }

        } catch (Exception e) {
            log.error("Failed to consume event", e);
        }
    }
}