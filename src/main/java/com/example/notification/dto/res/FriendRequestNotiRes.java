package com.example.notification.dto.res;

import com.example.notification.common.NotificationStatus;
import com.example.notification.common.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class FriendRequestNotiRes {
    private UUID friendUserId;
    private String friendUserProfile;
    private NotificationType type;
    private NotificationStatus status;
}