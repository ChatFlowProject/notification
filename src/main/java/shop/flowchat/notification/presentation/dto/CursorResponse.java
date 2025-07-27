package shop.flowchat.notification.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CursorResponse<T>(
        List<T> content,
        Boolean hasNext,
        LocalDateTime nextCursorCreatedAt,
        Long nextCursorId
) {
    public static <T> CursorResponse<T> from(List<T> content, Boolean hasNext, LocalDateTime nextCursorCreatedAt, Long nextCursorId) {
        return new CursorResponse<>(content, hasNext, nextCursorCreatedAt, nextCursorId);
    }
}
