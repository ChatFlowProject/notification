package com.example.notification.service;

import com.example.notification.dto.MemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "member", url = "https://dashboard.flowchat.shop:30001")
//public interface NotiServiceIn {
//    @GetMapping("/users/{username}")
//    MemberResponse getMemberByUsername(@PathVariable("username") String username);
//}
