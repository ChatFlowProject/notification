package shop.flowchat.notification.sse.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

public interface SseEmitterRepository {
    void add(UUID memberId, SseEmitter emitter);
    void send(UUID memberId, Object data);
    void remove(UUID memberId);
    SseEmitter get(UUID memberId);
}