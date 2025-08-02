package shop.flowchat.notification.external.kafka.consumer.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import shop.flowchat.notification.command.service.MemberReadModelCommandService;
import shop.flowchat.notification.external.kafka.payload.member.MemberEventPayload;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventConsumer {
    private final MemberReadModelCommandService service;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "member")
    public void consume(ConsumerRecord<String, String> record, @Header(name = "eventType", required = false) String eventType) {
        try {
            MemberEventPayload payload = objectMapper.readValue(record.value(), MemberEventPayload.class);

            if (eventType == null) {
                log.warn("eventType Header is null. Skipping record: {}", record);
                return;
            }

            switch (eventType) {
                case "signUp" -> service.create(payload);
                case "memberUpdate" -> service.updateProfile(payload);
                case "memberModifyStatus" -> service.updateStatus(payload);
                case "memberDelete" -> service.delete(payload);
                default -> log.warn("Unknown member eventType: {} Skipping record: {}", eventType, record);
            }

        } catch (Exception e) {
            log.error("Failed to consume event", e);
        }
    }

}