package shop.flowchat.notification.event.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import shop.flowchat.notification.external.dto.channel.ChannelUpdatedEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelEventConsumer {

    @KafkaListener(topics = "channel")
    public void consume(ChannelUpdatedEvent event) {

    }
}