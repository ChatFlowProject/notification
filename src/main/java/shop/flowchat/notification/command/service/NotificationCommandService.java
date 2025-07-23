package shop.flowchat.notification.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.common.dto.MemberInfo;
import shop.flowchat.notification.domain.member.MemberReadModel;
import shop.flowchat.notification.event.payload.FriendshipEventPayload;
import shop.flowchat.notification.query.MemberReadModelQuery;
import shop.flowchat.notification.sse.dto.FriendAcceptSseEvent;
import shop.flowchat.notification.sse.dto.FriendRequestSseEvent;
import shop.flowchat.notification.sse.service.NotificationSseService;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationCommandService {
    private final NotificationSseService notificationSseService;
    private final MemberReadModelQuery memberReadModelQuery;
    // private final NotificationRepository notificationRepository;

    public void createFriendRequestNoti(FriendshipEventPayload payload) {
        MemberReadModel receivingMember = memberReadModelQuery.getMemberById(payload.toMemberId());
        FriendRequestSseEvent ssePayload = FriendRequestSseEvent.from(payload.fromMemberId(), MemberInfo.from(receivingMember));
        notificationSseService.sendFriendRequestSse(ssePayload);
    }

    public void createFriendAcceptNoti(FriendshipEventPayload payload) {
        MemberReadModel receivingMember = memberReadModelQuery.getMemberById(payload.toMemberId());
        FriendAcceptSseEvent ssePayload = FriendAcceptSseEvent.from(payload.fromMemberId(), MemberInfo.from(receivingMember));
        notificationSseService.sendFriendAcceptSse(ssePayload);
    }

}
