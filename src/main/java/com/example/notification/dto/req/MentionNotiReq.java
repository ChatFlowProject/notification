package com.example.notification.dto.req;

import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@Getter
public class MentionNotiReq {
    private UUID mentionUserId;
    private UUID mentionedUserId;
}
