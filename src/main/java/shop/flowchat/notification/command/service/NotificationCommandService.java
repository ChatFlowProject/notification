package shop.flowchat.notification.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.common.dto.MemberInfo;
import shop.flowchat.notification.domain.member.MemberReadModel;
import shop.flowchat.notification.event.payload.FriendshipEventPayload;
import shop.flowchat.notification.query.MemberReadModelQuery;
import shop.flowchat.notification.sse.dto.FriendAcceptSseEvent;
import shop.flowchat.notification.sse.dto.FriendRequestSseEvent;
import shop.flowchat.notification.sse.repository.SseEmitterRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationCommandService {
    private final MemberReadModelQuery memberReadModelQuery;
    private final SseEmitterRepository sseEmitterRepository;

    public void createFriendRequestNoti(FriendshipEventPayload payload) {
        MemberReadModel receivingMember = memberReadModelQuery.getMemberById(payload.toMemberId());
        sseEmitterRepository.send(FriendRequestSseEvent.from(payload.fromMemberId(), MemberInfo.from(receivingMember)));
    }

    public void createFriendAcceptNoti(FriendshipEventPayload payload) {
        MemberReadModel receivingMember = memberReadModelQuery.getMemberById(payload.toMemberId());
        sseEmitterRepository.send(FriendAcceptSseEvent.from(payload.fromMemberId(), MemberInfo.from(receivingMember)));
    }

}
