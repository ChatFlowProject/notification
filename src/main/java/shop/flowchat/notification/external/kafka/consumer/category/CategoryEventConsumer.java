package shop.flowchat.notification.external.kafka.consumer.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import shop.flowchat.notification.command.service.TeamReadModelCommandService;
import shop.flowchat.notification.external.kafka.payload.category.CategoryEventPayload;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryEventConsumer {
    private final ObjectMapper objectMapper;
    private final TeamReadModelCommandService commandService;

    @KafkaListener(topics = "category")
    public void consume(ConsumerRecord<String, String> record,
                        @Header(name = "eventType", required = false) String eventType) {
        try {
            CategoryEventPayload payload = objectMapper.readValue(record.value(), CategoryEventPayload.class);

            if (eventType == null) {
                log.warn("eventType Header is null. Skipping record: {}", record);
                return;
            }

            switch (eventType) {
                case "categoryCreate" -> commandService.createCategory(payload);
                case "categoryDelete" -> commandService.deleteCategory(payload);
                default -> log.warn("Unknown category eventType: {} Skipping record: {}", eventType, record);
            }

        } catch (Exception e) {
            log.error("Failed to consume event", e);
        }
    }
}