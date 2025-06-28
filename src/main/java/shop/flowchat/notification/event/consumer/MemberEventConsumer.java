package shop.flowchat.notification.event.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import shop.flowchat.notification.external.dto.member.MemberUpdatedEvent;
import shop.flowchat.notification.query.NotificationMemberQuery;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventConsumer {
    private final NotificationMemberQuery memberQuery;

    @KafkaListener(topics = "member", groupId = "notification-group")
    public void consume(MemberUpdatedEvent event) {
        log.info("MemberUpdatedEvent : {}", event);
        memberQuery.update(event);
    }
}