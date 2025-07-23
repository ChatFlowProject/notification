package shop.flowchat.notification.sse.dto;

import shop.flowchat.notification.common.dto.MemberInfo;

import java.util.UUID;

public record FriendAcceptSseEvent(
        UUID senderId,
        MemberInfo receiver
) {
    public static FriendAcceptSseEvent from(UUID senderId, MemberInfo receiver) {
        return new FriendAcceptSseEvent(
                senderId,
                receiver
        );
    }
}
