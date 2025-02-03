package com.example.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    @Enumerated(EnumType.STRING)
    private NotiType type;

    private String message;
    private boolean Viewed; // 읽음 여부 플래그

    @CreationTimestamp
    private LocalDateTime createdAt;


}
