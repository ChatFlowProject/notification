package shop.flowchat.notification.sse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.flowchat.notification.sse.dto.MentionSseEvent;
import shop.flowchat.notification.sse.dto.MentionSsePayload;
import shop.flowchat.notification.sse.repository.SseEmitterRepository;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MentionSseService {
    private final SseEmitterRepository emitterRepository;

    public void send(MentionSseEvent event) {
        for (UUID receiverId : event.receiverIds()) {
            SseEmitter emitter = emitterRepository.get(receiverId);
            if (emitter != null) {
                try {
                    MentionSsePayload payload = MentionSsePayload.from(event);
                    emitter.send(SseEmitter.event()
                            .name("mention")
                            .data(payload));
                } catch (IOException e) {
                    emitterRepository.remove(receiverId);
                }
            }
        }
    }
}