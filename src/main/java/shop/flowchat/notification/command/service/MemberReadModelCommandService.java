package shop.flowchat.notification.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.domain.member.MemberReadModel;
import shop.flowchat.notification.external.kafka.payload.member.MemberEventPayload;
import shop.flowchat.notification.infrastructure.repository.member.MemberReadModelRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberReadModelCommandService {

    private final MemberReadModelRepository repository;

    public void create(MemberEventPayload payload) {
        if (repository.existsById(payload.id())) return;
        repository.save(MemberReadModel.create(payload));
    }

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

    public void delete(MemberEventPayload payload) {
        repository.deleteById(payload.id());
    }

}
