package shop.flowchat.notification.external.sse.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.flowchat.notification.external.sse.dto.SseEventPayload;

import java.util.UUID;

public interface SseEmitterRepository {
    void add(UUID memberId, SseEmitter emitter);
    void send(SseEventPayload payload);
    void remove(UUID memberId);
    SseEmitter get(UUID memberId);
}