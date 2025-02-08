package com.example.notification.dto.req;

import lombok.Data;

import java.util.UUID;

@Data
public class MentionNotiReq {
    private UUID mentionUserId;
    private UUID mentionedUserId;
}
