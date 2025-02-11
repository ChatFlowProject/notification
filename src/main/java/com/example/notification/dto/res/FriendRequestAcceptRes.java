package com.example.notification.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class FriendRequestAcceptRes {
    private UUID accepterId;
    private String accepterName;
}
