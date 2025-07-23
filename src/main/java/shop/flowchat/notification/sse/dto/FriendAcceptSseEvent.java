package shop.flowchat.notification.sse.dto;

import shop.flowchat.notification.common.dto.MemberInfo;

import java.util.UUID;

public record FriendAcceptSseEvent(
        UUID senderId,
        MemberInfo receiver
) implements SseEventPayload {
    public static FriendAcceptSseEvent from(UUID senderId, MemberInfo receiver) {
        return new FriendAcceptSseEvent(
                senderId,
                receiver
        );
    }

    @Override
    public UUID getReceiverId() {
        return receiver.id();
    }

    @Override
    public String getEventName() {
        return "friendAcceptNotification";
    }

}
