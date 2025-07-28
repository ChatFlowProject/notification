package shop.flowchat.notification.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import shop.flowchat.notification.common.dto.MemberInfo;
import shop.flowchat.notification.domain.notification.Notification;
import shop.flowchat.notification.domain.notification.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponse(
        @Schema(description = "알림 고유키", example = "5")
        Long id,
        @Schema(description = "알림 발신자")
        MemberInfo sender,
        @Schema(description = "알림 수신 회원 고유키", example = "98bd5bf6-848a-43d4-8683-205523c9e359")
        UUID receiverId,
        @Schema(description = "알림 유형", example = "FRIEND_REQUEST")
        NotificationType type,
        @Schema(description = "알림 메시지", example = "친구 요청을 받았어요.")
        String message,
        @Schema(description = "알림 확인 여부", example = "true")
        Boolean isRead,
        @Schema(description = "알림 생성 시간", example = "2025-07-28T21:38:46.711Z")
        LocalDateTime createdAt
) {
    public static NotificationResponse from(Notification noti) {
        return new NotificationResponse(
                noti.getId(),
                MemberInfo.from(noti.getSender()),
                noti.getReceiverId(),
                noti.getType(),
                noti.getMessage(),
                noti.getIsRead(),
                noti.getCreatedAt()
        );
    }
}
