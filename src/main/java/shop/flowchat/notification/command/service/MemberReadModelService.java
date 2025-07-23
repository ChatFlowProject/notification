package shop.flowchat.notification.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.domain.member.MemberReadModel;
import shop.flowchat.notification.event.payload.MemberEventPayload;
import shop.flowchat.notification.infrastructure.repository.member.MemberReadModelRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberReadModelService {

    private final MemberReadModelRepository repository;

    @Transactional
    public void create(MemberEventPayload payload) {
        if (repository.existsById(payload.id())) return;
        repository.save(MemberReadModel.create(payload));
    }

    @Transactional(readOnly = true)
    public MemberReadModel getMemberById(UUID memberId) {
        return repository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다 : " + memberId));
    }

    @Transactional
    public void updateProfile(MemberEventPayload payload) { // upsert
        repository.findById(payload.id())
                .ifPresentOrElse(
                        existingMember -> {
                            if (existingMember.isNewProfileUpdateEvent(payload.timestamp())) {
                                existingMember.updateProfile(payload);
                            }
                        },
                        () -> {
                            MemberReadModel memberReadModel = MemberReadModel.create(payload);
                            memberReadModel.updateProfile(payload);
                            repository.save(memberReadModel);
                        }
                );
    }

    @Transactional
    public void updateStatus(MemberEventPayload payload) { // upsert
        repository.findById(payload.id())
                .ifPresentOrElse(
                        existingMember -> {
                            if (existingMember.isNewStatusUpdateEvent(payload.timestamp())) {
                                existingMember.updateStatus(payload);
                            }
                        },
                        () -> {
                            MemberReadModel memberReadModel = MemberReadModel.create(payload);
                            memberReadModel.updateStatus(payload);
                            repository.save(memberReadModel);
                        }
                );
    }

    @Transactional
    public void delete(MemberEventPayload payload) {
        repository.deleteById(payload.id());
    }

}
