package com.example.notification.service;

import com.example.notification.common.ApiStatus;
import com.example.notification.common.NotificationStatus;
import com.example.notification.common.NotificationType;
import com.example.notification.config.MemberServiceClient;
import com.example.notification.dto.ApiResponse;
import com.example.notification.dto.MemberResponse;
import com.example.notification.dto.req.ChatMessageNotiReq;
import com.example.notification.dto.req.FriendRequestNotiReq;
import com.example.notification.dto.req.MentionNotiReq;
import com.example.notification.dto.res.ChatMessageNotiRes;
import com.example.notification.dto.res.FriendRequestNotiRes;
import com.example.notification.dto.res.MentionNotiRes;
import com.example.notification.entity.Notification;
import com.example.notification.entity.NotificationMessage;
import com.example.notification.repository.NotiRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final Map<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final MemberServiceClient memberServiceClient;
    private final NotiRepository notiRepository;

    // redis pub/sub 리스너 초기화
    @PostConstruct
    public void initRedisListener(){
        redisTemplate.getConnectionFactory().getConnection().subscribe((message, pattern)-> {
            String receivedMessage = new String(message.getBody());
            System.out.println("received message from redis"+receivedMessage);
            // 모든 활성화된 클라이언트로 메시지 전송
            emitters.forEach((userId, emitter)-> {
                try {
                    emitter.send(SseEmitter.event().name("notification").data(receivedMessage));
                } catch(IOException e){
                    emitters.remove(userId);
                }
            });
        }, "notification-channel".getBytes());
    }
    // 클라이언트가 SSE 연결 구독
    public SseEmitter subscribe(UUID userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // 무제한 대기 시간 설정
        emitters.put(userId, emitter); // 사용자 ID와 SseEmitter 매핑
        emitter.onCompletion(() -> emitters.remove(userId)); // 완료 시 제거
        emitter.onTimeout(() -> emitters.remove(userId)); // 타임아웃 시 제거
        return emitter;
    }

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

    // 멘션 알림
    public MentionNotiRes sendMentionNoti(MentionNotiReq request) {
        // 멘션된 사용자 정보 조회
        MemberResponse mentionedUser = findMemberById(request.getMentionUserId());

        // Redis를 통해 알림 전송
        String message = mentionedUser.getName() + "님이 멘션되었습니다.";
        sendNotiWithMessageToRedis(request.getMentionUserId(), message);

        // MentionNotiRes 객체 생성 및 반환
        return MentionNotiRes.builder()
                .serverId("serverId 1")
                .serverProfile("서버 사진1")
                .serverName("서버 이름1")
                .mentionUserId(request.getMentionUserId())
                .mentionUserName("이가은")
                .message("message1")
                .chatRoomName("chatRoomName1")
                .chatRoomId(1L)
                .categoryId(1L)
                .categoryName("category1")
                .mentionedUserId(request.getMentionUserId()) // 멘션된 사용자의 ID 설정
                .type(NotificationType.MENTION)
                .status(NotificationStatus.NOTREAD)
                .build();
    }

    // 친구 요청 시 알림 생성
    public FriendRequestNotiRes sendFriendRequestNoti(FriendRequestNotiReq friendRequestNotiReq) {
        // 1. 대상 사용자 정보 가져오기 (findMemberById 메서드 사용)
        MemberResponse targetUser = findMemberById(friendRequestNotiReq.getTargetUserId());

        // 2. Notification 객체 생성 및 데이터베이스에 저장
        Notification notification = Notification.builder()
                .recipientId(friendRequestNotiReq.getTargetUserId()) // 알림 대상 사용자 ID
                .type(NotificationType.FRIEND_REQUEST)              // 알림 타입: 친구 요청
                .status(NotificationStatus.NOTREAD)                 // 초기 상태: 읽지 않음
                .build();

        notiRepository.save(notification);
        // redis를 통해 친구 요청 알림 전송
        String message = targetUser.getName() + "님에게 친구 요청이 도착했습니다.";
        sendNotiWithMessageToRedis(friendRequestNotiReq.getTargetUserId(), message);

        // 3. FriendRequestNotiRes 객체 생성 및 반환
        return FriendRequestNotiRes.builder()
                .friendUserId(friendRequestNotiReq.getTargetUserId())         // 요청 대상 사용자 ID
                .type(NotificationType.FRIEND_REQUEST)                       // 알림 타입
                .status(NotificationStatus.NOTREAD)                          // 읽음 상태
                .build();
    }

    // 채팅 메시지 알림 전송
    // 채팅 메시지 알림 전송
    public ChatMessageNotiRes sendChatMessageNoti(ChatMessageNotiReq request) {
        // 1. 대상 사용자 정보 조회
        MemberResponse targetUser = findMemberById(request.getTargetUserId());
        MemberResponse senderUser = findMemberById(request.getSenderId()); // 발신자 정보 조회

        // 2. Redis를 통해 알림 전송
        String message = targetUser.getName() + ", 새로운 메시지가 있습니다: " + request.getMessage();
        sendNotiWithMessageToRedis(request.getTargetUserId(), message);

        // 3. ChatMessageNotiRes 객체 생성 및 반환
        return ChatMessageNotiRes.builder()
                .message(request.getMessage())
                .senderId(request.getSenderId()) // 발신자 ID 설정
                .senderName(senderUser.getName()) // 발신자 이름 설정
                .status(NotificationStatus.NOTREAD) // 초기 상태: 읽지 않음
                .type(NotificationType.CHAT_MESSAGE) // 알림 타입: 채팅 메시지
                .build();
    }
    private void sendNotiToRedis(UUID userId) {
        redisTemplate.convertAndSend("notification-channel", "멘션 되었습니다.");
    }

    private void sendNotiWithMessageToRedis(UUID userId, String message) {
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

