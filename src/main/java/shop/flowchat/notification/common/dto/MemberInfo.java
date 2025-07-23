package shop.flowchat.notification.common.dto;

import java.util.UUID;
import shop.flowchat.notification.domain.member.MemberReadModel;

public record MemberInfo(
        UUID id,
        String name,
        String avatarUrl
) {
    public static MemberInfo from(MemberReadModel member) {
        return new MemberInfo(
                member.getId(),
                member.getName(),
                member.getAvatarUrl()
        );
    }
}
