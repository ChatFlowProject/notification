package com.example.notification.dto.req;

import lombok.Data;

import java.util.UUID;

@Data
public class ChatMessageNotiReq {
    private UUID targetUserId;
    private UUID senderId;
    private String message;
}
