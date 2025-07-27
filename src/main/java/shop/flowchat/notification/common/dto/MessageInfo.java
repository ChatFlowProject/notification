package shop.flowchat.notification.common.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import shop.flowchat.notification.domain.message.MessageReadModel;
import shop.flowchat.notification.event.payload.MentionEventPayload;

public record MessageInfo(
        Long id,
        UUID chatId,
        String content,
        LocalDateTime createdAt
) {
    public static MessageInfo from(MentionEventPayload payload) {
        return new MessageInfo(
                payload.messageId(),
                payload.chatId(),
                payload.content(),
                payload.createdAt()
        );
    }
    public static MessageInfo from(MessageReadModel message) {
        return new MessageInfo(
                message.getId(),
                message.getChatId(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}