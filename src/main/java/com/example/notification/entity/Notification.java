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
@AllArgsConstructor
@Entity
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    private UUID recipientId; // 알림을 받은 사용자 ID

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type; // 알림 종류

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status; // 읽음 상태

    private String message; // 알림 메시지

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성 시간
}

