package com.example.notification.service;

import com.example.notification.common.ApiStatus;
import com.example.notification.config.MemberServiceClient;
import com.example.notification.dto.ApiResponse;
import com.example.notification.dto.MemberResponse;
import com.example.notification.dto.req.ChatMessageNotiReq;
import com.example.notification.dto.req.FriendRequestNotiReq;
import com.example.notification.dto.req.MentionNotiReq;
import com.example.notification.entity.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;



@Service
@RequiredArgsConstructor
public class NotiService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
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

    // 클라이언트가 SSE 연결 구독
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(userId, emitter);
        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        redisTemplate.convertAndSend("notification-channel", "user" + userId + " connected");
        return emitter;
    }

    // 멘션 알림 전송
    public void sendMentionNoti(MentionNotiReq request) {
        MemberResponse mentionedUser = findMemberById(request.getMentionUserId());
        String message = mentionedUser.getName() + ", you were mentioned: " + request.getMessage();
        sendNotiToRedis(request.getMentionUserId(), message);
    }

    // 친구 요청 알림 전송
    public void sendFriendRequestNoti(FriendRequestNotiReq request) {
        MemberResponse targetUser = findMemberById(request.getTargetUserId());
        String message = targetUser.getName() + ", 친구 요청을 받았습니다: " + request.getMessage();
        sendNotiToRedis(request.getTargetUserId(), message);
    }

    // 채팅 메시지 알림 전송
    public void sendChatMessageNoti(ChatMessageNotiReq request) {
        MemberResponse targetUser = findMemberById(request.getTargetUserId());
        String message = targetUser.getName() + ", 새로운 메시지가 있습니다: " + request.getMessage();
        sendNotiToRedis(request.getTargetUserId(), message);
    }

    private void sendNotiToRedis(UUID userId, String message) {
        redisTemplate.convertAndSend("notification-channel", new NotificationMessage(userId, message));
    }

    @EventListener
    public void handleRedisMessage(NotificationMessage message) {
        SseEmitter emitter = emitters.get(message.getUserId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(message.getMessage()));
            } catch (IOException e) {
                emitters.remove(message.getUserId());
            }
        }
    }
}

