package shop.flowchat.notification.sse.dto;

import java.util.UUID;

public interface SseEventPayload {
    UUID getReceiverId();
    String getEventName();
}
