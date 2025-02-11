package com.example.notification.repository;


import com.example.notification.common.NotificationStatus;
import com.example.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
    public interface NotiRepository extends JpaRepository<Notification, UUID> {
        List<Notification> findByrecipientId(UUID userId);
        List<Notification> findByrecipientIdAndStatus(UUID recipientId, NotificationStatus status); // 특정 사용자와 읽음 상태로 조회
}
