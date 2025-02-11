package com.example.notification.controller;

import com.example.notification.common.BaseResponse;
import com.example.notification.dto.req.ChatMessageNotiReq;
import com.example.notification.dto.req.FriendRequestNotiReq;
import com.example.notification.dto.req.MentionNotiReq;
import com.example.notification.dto.res.FriendRequestNotiRes;
import com.example.notification.dto.res.MentionNotiRes;
import com.example.notification.service.NotiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequestMapping("/noti")
@RequiredArgsConstructor
public class NotiController {
    private final NotiService notificationService;

    // 클라이언트가 SSE 연결을 구독
    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long userId) {
        return notificationService.subscribe(userId);
    }

    // 2. 멘션 알림 생성 API
    @PostMapping("/mention")
    public BaseResponse<MentionNotiRes> sendMentionNotification(@RequestBody MentionNotiReq request) {
        MentionNotiRes mentionNotiRes = notificationService.sendMentionNoti(request);
        return new BaseResponse<>(mentionNotiRes);
    }

    // 3. 친구 요청시 알림 생성 API
    @PostMapping("/friend-request")
    public BaseResponse<FriendRequestNotiRes> sendFriendRequestNotification(@RequestBody FriendRequestNotiReq request) {
        FriendRequestNotiRes friendRequestNotiRes = notificationService.sendFriendRequestNoti(request);
        return new BaseResponse<>(friendRequestNotiRes);
    }

    // 4. 채팅 메시지 알림 생성 API
    @PostMapping("/chat-message")
    public ResponseEntity<Void> sendChatMessageNotification(@RequestBody ChatMessageNotiReq request) {
        notificationService.sendChatMessageNoti(request);
        return ResponseEntity.ok().build();
    }
}
