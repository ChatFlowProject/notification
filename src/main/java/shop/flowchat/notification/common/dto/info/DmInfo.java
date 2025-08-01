package shop.flowchat.notification.common.dto.info;

import java.util.UUID;
import shop.flowchat.notification.domain.notification.Notification;
import shop.flowchat.notification.external.kafka.payload.message.TeamInviteEventPayload;

public record DmInfo (
    UUID chatId,
    Long messageId
) {
    public static DmInfo from(Notification notification) {
        return new DmInfo(
                notification.getChatId(),
                notification.getMessageId()
        );
    }
}
