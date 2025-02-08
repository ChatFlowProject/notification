package com.example.notification.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class MentionNotiRes {
    private UUID mentionedUserId;
}
