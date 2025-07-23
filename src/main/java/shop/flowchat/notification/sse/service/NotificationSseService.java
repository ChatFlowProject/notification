package shop.flowchat.notification.sse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.flowchat.notification.sse.dto.FriendAcceptSseEvent;
import shop.flowchat.notification.sse.dto.FriendRequestSseEvent;
import shop.flowchat.notification.sse.repository.SseEmitterRepository;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationSseService {
    private final SseEmitterRepository emitterRepository;

    public void sendFriendRequestSse(FriendRequestSseEvent payload) {
        UUID receiverId = payload.receiver().id();
        SseEmitter emitter = emitterRepository.get(receiverId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("friendRequestNotification")
                        .data(payload));
            } catch (IOException e) {
                emitterRepository.remove(receiverId);
            }
        }
    }

    public void sendFriendAcceptSse(FriendAcceptSseEvent payload) {
        UUID receiverId = payload.receiver().id();
        SseEmitter emitter = emitterRepository.get(receiverId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("friendAcceptNotification")
                        .data(payload));
            } catch (IOException e) {
                emitterRepository.remove(receiverId);
            }
        }
    }
}
