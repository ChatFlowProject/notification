package shop.flowchat.notification.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.common.dto.MemberInfo;
import shop.flowchat.notification.domain.member.NotificationMember;
import shop.flowchat.notification.external.dto.member.MemberUpdatedEvent;
import shop.flowchat.notification.infrastructure.repository.member.NotificationMemberRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationMemberQuery {
    private final NotificationMemberRepository memberRepository;

    public MemberInfo findById(UUID memberId) {
        NotificationMember member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다 : " + memberId));

        return MemberInfo.from(member);
    }
    public void update(MemberUpdatedEvent event) {
        NotificationMember member = memberRepository.findById(event.id())
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다 : " + event.id()));

        member.update(event.name(), event.avatarUrl());
        memberRepository.save(member);
    }
}
