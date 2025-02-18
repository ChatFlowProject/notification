package com.example.notification.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ReadMyNotiRes {
    private String message;
    private Long idx;
    private int type;
    private int status;
    private LocalDateTime time;
    private UUID userNotiId;
}
