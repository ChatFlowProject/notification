package shop.flowchat.notification.command.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.domain.message.AttachmentReadModel;
import shop.flowchat.notification.domain.message.MessageReadModel;
import shop.flowchat.notification.event.payload.MentionEventPayload;
import shop.flowchat.notification.infrastructure.repository.message.AttachmentReadModelRepository;
import shop.flowchat.notification.infrastructure.repository.message.MessageReadModelRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageReadModelCommandService {
    private final MessageReadModelRepository messageRepository;
    private final AttachmentReadModelRepository attachmentRepository;

    public void createMessage(MentionEventPayload payload) {
        if (messageRepository.existsById(payload.messageId())) return;
        MessageReadModel message = messageRepository.save(MessageReadModel.create(payload));
        if (payload.attachments() != null && !payload.attachments().isEmpty()) {
            List<AttachmentReadModel> attachments = payload.attachments().stream()
                    .map(attachmentDto -> AttachmentReadModel.create(message.getId(), attachmentDto))
                    .toList();
            attachmentRepository.saveAll(attachments);
        }
    }

    public void updateMessage(MentionEventPayload payload) {
        messageRepository.findById(payload.messageId())
                .ifPresentOrElse(
                        existingMessage -> {
                            if (existingMessage.isUpdated(payload.updatedAt())) {
                                existingMessage.updateContent(payload.content());
                            }
                        },
                        () -> {
                            MessageReadModel messageReadModel = MessageReadModel.create(payload);
                            messageReadModel.updateContent(payload.content());
                            messageRepository.save(messageReadModel);
                        }
                );
    }

    public void deleteMessage(MentionEventPayload payload) {
        if (!messageRepository.existsById(payload.messageId())) return;
        messageRepository.deleteById(payload.messageId());
        attachmentRepository.deleteByMessageId(payload.messageId());
    }
}
