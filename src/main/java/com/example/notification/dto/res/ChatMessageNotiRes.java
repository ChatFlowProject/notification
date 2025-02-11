package com.example.notification.dto.res;

import com.example.notification.common.NotificationStatus;
import com.example.notification.common.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class ChatMessageNotiRes {
    private String message;
    private UUID senderId;
    private String senderName;
    private NotificationStatus status;
    private NotificationType type;
}
