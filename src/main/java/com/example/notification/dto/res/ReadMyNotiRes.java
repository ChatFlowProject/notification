package com.example.notification.dto.res;

import com.example.notification.common.MemberType;
import com.example.notification.common.NotificationStatus;
import com.example.notification.common.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ReadMyNotiRes {
    private String message;
    private Long idx;
    private NotificationType type;
    private NotificationStatus status;
    private LocalDateTime time;
    private UUID userNotiId;
}
