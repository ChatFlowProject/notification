package shop.flowchat.notification.external.sse.dto;

import shop.flowchat.notification.common.dto.info.MemberInfo;

import java.util.UUID;

public record FriendAcceptSseEvent(
        MemberInfo sender,
        UUID receiverId
) implements SseEventPayload {
    public static FriendAcceptSseEvent from(MemberInfo sender, UUID receiverId) {
        return new FriendAcceptSseEvent(
                sender,
                receiverId
        );
    }

    @Override
    public UUID getReceiverId() {
        return receiverId;
    }

    @Override
    public String getEventName() {
        return "friendAcceptNotification";
    }

}
