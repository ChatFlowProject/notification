package shop.flowchat.notification.common.dto;

import shop.flowchat.notification.domain.team.NotificationTeam;

import java.util.UUID;

public record TeamInfo(
    UUID id,
    String name,
    String iconUrl
) {
    public static TeamInfo from(NotificationTeam team) {
        return new TeamInfo(
                team.getId(),
                team.getName(),
                team.getImageUrl()
        );
    }
}
