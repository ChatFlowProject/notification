package shop.flowchat.notification.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import shop.flowchat.notification.command.service.NotificationCommandService;
import shop.flowchat.notification.event.payload.FriendshipEventPayload;

@Slf4j
@RequiredArgsConstructor
@Service
public class FriendshipEventConsumer {
    private final NotificationCommandService service;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "friendship",
            containerFactory = "stringKafkaListener"
    )
    public void consume(ConsumerRecord<String, String> record, @Header(name = "eventType", required = false) String eventType) {
        try {
            FriendshipEventPayload payload = objectMapper.readValue(record.value(), FriendshipEventPayload.class);

            if (eventType == null) {
                log.warn("eventType Header is null. Skipping record: {}", record);
                return;
            }

            switch (eventType) {
                case "friendshipRequest" -> service.createFriendRequestNoti(payload);
                case "friendshipAccept" -> service.createFriendAcceptNoti(payload);
                default -> log.warn("Unhandled friendship eventType: {}", eventType);
            }

        } catch (Exception e) {
            log.error("Failed to consume at FriendshipEventConsumer", e);
        }
    }

}
