package com.example.notification.domain;

import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class Notification {
    @Id
    private String id;
    private String senderId;
    private String senderName;
    private String content;
    private String channelId;
    private String channelName;
    private String target;
    private LocalDateTime localDateTime;

    public Notification(String id, String senderId, String senderName, String content, String channelId, String channelName, String target, LocalDateTime localDateTime) {
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.content = content;
        this.channelId = channelId;
        this.channelName = channelName;
        this.target = target;
        this.localDateTime = localDateTime;
    }
}
