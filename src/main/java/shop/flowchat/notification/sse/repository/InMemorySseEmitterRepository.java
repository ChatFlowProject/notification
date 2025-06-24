package shop.flowchat.notification.sse.repository;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemorySseEmitterRepository implements SseEmitterRepository {

    private final Map<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public void add(UUID memberId, SseEmitter emitter) {
        emitters.put(memberId, emitter);
        emitter.onCompletion(() -> emitters.remove(memberId));
        emitter.onTimeout(() -> emitters.remove(memberId));
        emitter.onError((e) -> emitters.remove(memberId));
    }

    @Override
    public void send(UUID memberId, Object data) {
        SseEmitter emitter = emitters.get(memberId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("mention-notification")
                        .data(data));
            } catch (IOException e) {
                emitters.remove(memberId);
            }
        }
    }

    @Override
    public void remove(UUID memberId) {
        emitters.remove(memberId);
    }

    @Override
    public SseEmitter get(UUID memberId) {
        return emitters.get(memberId);
    }
}