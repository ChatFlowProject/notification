package shop.flowchat.notification.domain.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum NotificationType {
    FRIEND_ACCEPTED("친구수락"),
    FRIEND_REQUEST("친구요청"),
    TEAM_INVITE("팀초대"),
    MESSAGE("메시지");

    private String name;

}
