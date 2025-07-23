package shop.flowchat.notification.sse.dto;

import shop.flowchat.notification.common.dto.MemberInfo;

import java.util.UUID;

public record FriendRequestSseEvent(
        UUID senderId,
        MemberInfo receiver
) implements SseEventPayload {
    public static FriendRequestSseEvent from(UUID senderId, MemberInfo receiver) {
        return new FriendRequestSseEvent(
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
        return "friendRequestNotification";
    }

}
