package shop.flowchat.notification.external.sse.dto;

import java.util.UUID;

public interface SseEventPayload {
    UUID getReceiverId();
    String getEventName();
}
