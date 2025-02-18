package com.example.notification.dto.req;

import com.example.notification.common.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class NotiReq {
    List<UUID> receiverIds;
    UUID senderId;
    Long notiId;
    NotificationType type;
}
