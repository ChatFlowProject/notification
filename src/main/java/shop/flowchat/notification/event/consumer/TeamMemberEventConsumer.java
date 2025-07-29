package shop.flowchat.notification.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import shop.flowchat.notification.command.service.TeamReadModelCommandService;
import shop.flowchat.notification.event.payload.TeamMemberEventPayload;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamMemberEventConsumer {
    private final ObjectMapper objectMapper;
    private final TeamReadModelCommandService commandService;

    @KafkaListener(topics = "teamMember")
    public void consume(ConsumerRecord<String, String> record, @Header(name = "eventType", required = false) String eventType) {
        try {
            TeamMemberEventPayload payload = objectMapper.readValue(record.value(), TeamMemberEventPayload.class);

            if (eventType == null) {
                log.warn("eventType Header is null. Skipping record: {}", record);
                return;
            }

            switch (eventType) {
                case "teamMemberCreate" -> commandService.createTeamMember(payload);
                case "teamMemberDelete" -> commandService.deleteTeamMember(payload);
                default -> log.warn("Unknown team member eventType: {} Skipping record: {}", eventType, record);
            }

        } catch (Exception e) {
            log.error("Failed to consume event", e);
        }
    }
}
