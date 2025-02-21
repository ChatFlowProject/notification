package com.example.notification.service;

import com.example.notification.common.ApiStatus;
import com.example.notification.common.NotificationStatus;
import com.example.notification.common.NotificationType;
import com.example.notification.config.member_server.MemberServiceClient;
import com.example.notification.dto.ApiResponse;
import com.example.notification.dto.MemberResponse;
import com.example.notification.dto.res.*;
import com.example.notification.entity.Notification;
import com.example.notification.repository.NotiRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NotiService {
    private final Map<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final NotiRepository notiRepository;
    private final MemberServiceClient memberServiceClient;

    // 모든 멤버 조회 메서드
    public List<MemberResponse> getAllMembers() {
        ApiResponse<List<MemberResponse>> response = memberServiceClient.getAllMembers();

        if (response.status() != ApiStatus.SUCCESS || response.data() == null) {
            throw new RuntimeException("Failed to fetch members: " + response.message());
        }

        return response.data();
    }

    // 특정 멤버 정보를 ID로 조회하는 메서드
    private MemberResponse findMemberById(UUID memberId) {
        List<MemberResponse> members = getAllMembers();

        return members.stream()
                .filter(member -> member.getId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Member not found for ID: " + memberId));
    }


    // 클라이언트가 SSE 연결을 구독
    public SseEmitter subscribe(UUID userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(userId, emitter);

        // 연결 종료 시 처리
        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));

        // 연결 성공 메시지 전송 (옵션)
        try {
            emitter.send(SseEmitter.event().name("connect").data("Connected successfully"));
        } catch (IOException e) {
            emitters.remove(userId);
        }

        return emitter;
    }

    // 특정 클라이언트에게 이벤트 전송
    public void sendEventsToClients(List<UUID> receiverIds, UUID senderId, NotificationType type) throws JsonProcessingException {
        // 발신자 정보 조회
        MemberResponse sender = findMemberById(senderId);

        // 알림 메시지 생성
        String message = generateNotificationMessage(type, sender);

        for (UUID receiverId : receiverIds) {
            // 알림 엔티티 생성 및 저장
            Notification notification = Notification.builder()
                    .recipientId(receiverId)
                    .type(type)
                    .status(NotificationStatus.NOTREAD) // 초기 상태: 읽지 않음
                    .message(message)
                    .build();

            notiRepository.save(notification); // 알림 저장

            // 실시간 전송
            sendNotificationToReceiver(receiverId, sender, notification, type);
        }
    }

    // 알림 메시지 생성 (알림 타입에 따라 다르게 설정 가능)
    private String generateNotificationMessage(NotificationType type, MemberResponse sender) {
        switch (type) {
            case FRIEND_REQUEST:
                return sender.getName() + "님이 친구 요청을 보냈습니다.";
            case CHAT_MESSAGE:
                return sender.getName() + "님으로부터 새로운 메시지가 도착했습니다.";
            case MENTION:
                return sender.getName() + "님이 당신을 멘션했습니다.";
            default:
                return "새로운 알림이 도착했습니다.";
        }
    }

    // 공통: 특정 사용자에게 알림 전송
    private void sendNotificationToReceiver(UUID receiverId, MemberResponse sender ,Notification notification, NotificationType type) {
        SseEmitter emitter = emitters.get(receiverId);
        if (emitter != null) {
            Map<String, Object> notificationData = new HashMap<>();
            notificationData.put("message", notification.getMessage());
            notificationData.put("type", type);

            try {
                emitter.send(SseEmitter.event().name("notification").data(notificationData));
            } catch (IOException e) {
                // 연결이 끊어진 경우 제거
                emitters.remove(receiverId);
            }
        }
    }

    // 특정 알림 ID로 알림 조회
    private Notification findNotificationById(Long notificationId) {
        return notiRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found for ID: " + notificationId));
    }

    // 나의 알림 조회
    public List<ReadMyNotiRes> readMyNotifications(UUID userId) {
        List<Notification> notifications = notiRepository.findByRecipientId(userId);
        return notifications.stream()
                .map(notification -> ReadMyNotiRes.builder()
                        .message(notification.getMessage())
                        .idx(notification.getId())
                        .type(notification.getType().ordinal())
                        .status(notification.getStatus().ordinal())
                        .time(notification.getCreatedAt())
                        .userNotiId(userId)
                        .build())
                .toList();
    }

    // 안 읽은 알림 조회
    public List<ReadMyNotiRes> getUnreadNotifications(UUID userId) {
        List<Notification> notifications = notiRepository.findByRecipientIdAndStatus(userId, NotificationStatus.NOTREAD);
        return notifications.stream()
                .map(notification -> ReadMyNotiRes.builder()
                        .message(notification.getMessage())
                        .idx(notification.getId())
                        .type(notification.getType().ordinal())
                        .status(notification.getStatus().ordinal())
                        .time(notification.getCreatedAt())
                        .userNotiId(userId)
                        .build())
                .toList();
    }
    // 알림 상태 업데이트 (읽음 처리)
    public void updateNotificationStatus(Long notiId) {
        Notification notification = findNotificationById(notiId); // 알림 조회
        notification.setStatus(NotificationStatus.READ); // 상태를 읽음(READ)으로 변경
        notiRepository.save(notification); // 변경된 상태를 저장
    }
}
