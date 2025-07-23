package shop.flowchat.notification.sse.dto;

import shop.flowchat.notification.common.dto.MemberInfo;

import java.util.UUID;

public record FriendRequestSseEvent(
        UUID senderId,
        MemberInfo receiver
) {
    public static FriendRequestSseEvent from(UUID senderId, MemberInfo receiver) {
        return new FriendRequestSseEvent(
                senderId,
                receiver
        );
    }
}
