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
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID recipientId;


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

