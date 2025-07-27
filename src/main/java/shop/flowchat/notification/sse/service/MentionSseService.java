package shop.flowchat.notification.sse.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.flowchat.notification.common.dto.MentionMessageResponse;
import shop.flowchat.notification.sse.repository.SseEmitterRepository;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MentionSseService {
    private final SseEmitterRepository emitterRepository;

    public void send(List<UUID>receiverIds, MentionMessageResponse response) {
        for (UUID receiverId : receiverIds) {
            SseEmitter emitter = emitterRepository.get(receiverId);
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("mention")
                            .data(response));
                } catch (IOException e) {
                    emitterRepository.remove(receiverId);
                }
            }
        }
    }
}