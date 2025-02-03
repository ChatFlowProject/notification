package com.example.notification.config;

import com.example.notification.dto.MemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "member-service",
        url = "http://flowchat.shop:30002", // 실제 멤버 서비스 경로
        configuration = FeignConfig.class
)
public interface MemberServiceClient {

    // 회원 전체 조회 API 호출
    @GetMapping("/admin/members")
    ResponseEntity<List<MemberResponse>> getAllMembers();

    // 특정 회원 정보 조회 API 호출
    @GetMapping("/admin/members/{memberId}")
    ResponseEntity<MemberResponse> getMemberById(@PathVariable("memberId") String memberId);
}
