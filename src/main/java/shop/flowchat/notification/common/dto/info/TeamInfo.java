package shop.flowchat.notification.common.dto.info;

import shop.flowchat.notification.domain.team.TeamReadModel;

import java.util.UUID;

public record TeamInfo(
    UUID id,
    String name,
    String iconUrl
) {
    public static TeamInfo from(TeamReadModel team) {
        return new TeamInfo(
                team.getId(),
                team.getName(),
                team.getIconUrl()
        );
    }
}
