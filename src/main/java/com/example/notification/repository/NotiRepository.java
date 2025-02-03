package com.example.notification.repository;


import com.example.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotiRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
}
