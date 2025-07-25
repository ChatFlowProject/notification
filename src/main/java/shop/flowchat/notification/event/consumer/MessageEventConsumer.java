package shop.flowchat.notification.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import shop.flowchat.notification.command.service.MessageReadModelCommandService;
import shop.flowchat.notification.event.payload.MentionEventPayload;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageEventConsumer {
    private final MessageReadModelCommandService commandService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "mention")
    public void consume(ConsumerRecord<String, String> record, @Header(name = "eventType", required = false) String eventType) {
        try {
            MentionEventPayload payload = objectMapper.readValue(record.value(), MentionEventPayload.class);

            if (eventType == null) {
                log.warn("eventType Header is null. Skipping record: {}", record);
                return;
            }

            switch (eventType) {
                case "mentionCreate" -> commandService.createMessage(payload);
                case "mentionUpdate" -> commandService.updateMessage(payload);
                case "mentionDelete" -> commandService.deleteMessage(payload);
                default -> log.warn("Unknown member eventType: {} Skipping record: {}", eventType, record);
            }

        } catch (Exception e) {
            log.error("Failed to consume event", e);
        }
    }
}