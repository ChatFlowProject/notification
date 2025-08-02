package shop.flowchat.notification.external.kafka.consumer.channel;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import shop.flowchat.notification.command.service.TeamReadModelCommandService;
import shop.flowchat.notification.external.kafka.payload.channel.ChannelEventPayload;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelEventConsumer {
    private final ObjectMapper objectMapper;
    private final TeamReadModelCommandService commandService;

    @KafkaListener(topics = "channel")
    public void consume(ConsumerRecord<String, String> record, @Header(name = "eventType", required = false) String eventType) {
        try {
            ChannelEventPayload payload = objectMapper.readValue(record.value(), ChannelEventPayload.class);

            if (eventType == null) {
                log.warn("eventType Header is null. Skipping record: {}", record);
                return;
            }

            switch (eventType) {
                case "channelCreate" -> commandService.createChannel(payload);
                case "channelUpdate" -> commandService.updateChannel(payload);
                case "channelDelete" -> commandService.deleteChannel(payload);
                default -> log.warn("Unknown channel eventType: {} Skipping record: {}", eventType, record);
            }

        } catch (Exception e) {
            log.error("Failed to consume event", e);
        }
    }
}