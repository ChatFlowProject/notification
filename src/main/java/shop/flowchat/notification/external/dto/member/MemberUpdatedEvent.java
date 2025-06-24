package shop.flowchat.notification.external.dto.member;

import java.util.UUID;

public record MemberUpdatedEvent(
        UUID id,
        String name,
        String avatarUrl
) {}
