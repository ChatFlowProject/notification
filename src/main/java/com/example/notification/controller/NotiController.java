package com.example.notification.controller;

import com.example.notification.common.ApiStatus;
import com.example.notification.common.BaseResponse;
import com.example.notification.common.BaseResponseStatus;
import com.example.notification.config.member_server.MemberServiceClient;
import com.example.notification.dto.ApiResponse;
import com.example.notification.dto.MemberResponse;
import com.example.notification.dto.req.*;
import com.example.notification.dto.res.*;
import com.example.notification.service.NotiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/noti")
@RequiredArgsConstructor
public class NotiController {
    private final NotiService notificationService;
    private final MemberServiceClient memberServiceClient;

    // 클라이언트가 SSE 연결을 구독
    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable UUID userId) {
        return notificationService.subscribe(userId);
    }

    // 특정 클라이언트에게 이벤트 전송
    @PostMapping("/send")
    public void sendEventToClient(@RequestBody NotiReq notiReq) throws JsonProcessingException {
        notificationService.sendEventsToClients(notiReq.getReceiverIds(), notiReq.getSenderId(), notiReq.getType());
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


    // 나의 알림 조회
    @GetMapping("/my")
    public BaseResponse<List<ReadMyNotiRes>> readMyNoti(@RequestParam UUID userId) {
        MemberResponse user = findMemberById(userId);
        List<ReadMyNotiRes> response = notificationService.readMyNotifications(user);
        return new BaseResponse<>(response);
    }

    // 알림 상태 업데이트(읽음 처리)
    @PatchMapping("/update/{notiId}")
    public BaseResponse<Void> updateNotificationStatus(@PathVariable Long notiId) {
        notificationService.updateNotificationStatus(notiId);
        return new BaseResponse<>(BaseResponseStatus.NOTI_UPDATE_SUCCESS);
    }
}



