package com.example.notification.entity;

import com.example.notification.common.NotificationStatus;
import com.example.notification.common.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID recipientId; // 알림을 받은 사용자 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type; // 알림 종류

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status; // 읽음 상태

    private String message; // 알림 메시지

    private LocalDateTime createdAt = LocalDateTime.now(); // 생성 시간
}
