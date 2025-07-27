package shop.flowchat.notification.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.flowchat.notification.command.service.NotificationCommandService;
import shop.flowchat.notification.presentation.dto.CursorResponse;
import shop.flowchat.notification.presentation.dto.NotificationResponse;
import shop.flowchat.notification.query.NotificationQuery;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Notification Service API (인증 토큰 필요)")
@RequiredArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationQuery notificationQuery;
    private final NotificationCommandService notificationCommandService;

    @Operation(summary = "모든 알림 조회")
    @GetMapping
    public ResponseEntity<CursorResponse<NotificationResponse>> getAllNotifications(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            @RequestParam(required = false) Long notificationId,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(notificationQuery.getAllNotifications(token, dateTime, notificationId, size));
    }

    @Operation(summary = "읽지 않은 알림 커서 기반 조회")
    @GetMapping("/unread")
    public ResponseEntity<CursorResponse<NotificationResponse>> getUnreadNotifications(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            @RequestParam(required = false) Long notificationId,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(notificationQuery.getUnreadNotifications(token, dateTime, notificationId, size));
    }

    @Operation(summary = "알림 읽음 처리")
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markNotificationAsRead(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @PathVariable("notificationId") Long notificationId) {
        notificationCommandService.markNotificationAsRead(token, notificationId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "여러 알림 읽음 처리")
    @PatchMapping("/read")
    public ResponseEntity<Void> markNotificationsAsRead(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @RequestBody List<Long> notificationIds) {
        notificationCommandService.markNotificationsAsRead(token, notificationIds);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "전체 알림 읽음 처리")
    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllNotificationsAsRead(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        notificationCommandService.markAllNotificationsAsRead(token);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "알림 삭제")
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @PathVariable("notificationId") Long notificationId) {
        notificationCommandService.deleteNotification(token, notificationId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "모든 읽은 알림 삭제")
    @DeleteMapping("/read")
    public ResponseEntity<Void> deleteNotifications(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        notificationCommandService.deleteReadNotifications(token);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "모든 알림 삭제")
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllNotifications(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        notificationCommandService.deleteAllNotifications(token);
        return ResponseEntity.ok().build();
    }

}
