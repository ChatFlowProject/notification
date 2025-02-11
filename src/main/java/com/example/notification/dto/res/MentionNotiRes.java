package com.example.notification.dto.res;

import com.example.notification.common.NotificationStatus;
import com.example.notification.common.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class MentionNotiRes {
    private String serverId;
    private String serverProfile;
    private String serverName;
    private UUID mentionUserId;
    private String mentionUserName;
    private String message;
    private String chatRoomName;
    private Long chatRoomId;
    private Long categoryId;
    private String categoryName;
    private UUID mentionedUserId;
    private NotificationType type;
    private NotificationStatus status;
}
