package shop.flowchat.notification.common.dto;

import java.util.UUID;
import shop.flowchat.notification.domain.member.NotificationMember;

public record MemberInfo(
        UUID id,
        String name,
        String avatarUrl
) {
    public static MemberInfo from(NotificationMember member) {
        return new MemberInfo(
                member.getId(),
                member.getName(),
                member.getAvatarUrl()
        );
    }
}
