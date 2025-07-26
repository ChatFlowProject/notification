package shop.flowchat.notification.query;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.common.dto.MemberInfo;
import shop.flowchat.notification.domain.member.MemberReadModel;
import shop.flowchat.notification.infrastructure.repository.member.MemberReadModelRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberReadModelQuery {
    private final MemberReadModelRepository repository;

    public MemberReadModel getMemberById(UUID memberId) {
        return repository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다 : " + memberId));
    }

    public List<MemberReadModel> getAllMembersById(List<UUID> memberIds) {
        return repository.findAllById(memberIds);
    }

    public MemberInfo getMemberInfoById(UUID memberId) {
        return repository.findById(memberId)
                .map(MemberInfo::from)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다 : " + memberId));
    }
}
