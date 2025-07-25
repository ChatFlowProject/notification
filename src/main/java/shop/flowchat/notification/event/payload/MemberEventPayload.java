package shop.flowchat.notification.event.payload;


import shop.flowchat.notification.domain.member.MemberReadModelState;

import java.time.LocalDateTime;
import java.util.UUID;

public record MemberEventPayload(
        UUID id,
        String nickname,
        String name,
        String avatarUrl,
        MemberReadModelState state,
        LocalDateTime createdAt,
        LocalDateTime timestamp
) {
}
