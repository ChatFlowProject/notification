package shop.flowchat.notification.event.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import shop.flowchat.notification.command.dto.MentionCreateEvent;
import shop.flowchat.notification.command.service.MentionCommandService;

@Component
@RequiredArgsConstructor
public class MentionCreateConsumer {
    private final MentionCommandService mentionCommandService;

    @KafkaListener(topics = "mention", groupId = "notification-group")
    public void mentionCreateEvent(MentionCreateEvent event) {
        mentionCommandService.createMention(event);
    }
}