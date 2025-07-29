package shop.flowchat.notification.common.dto.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum AttachmentType {
    IMAGE("image"),
    FILE("file");

    private final String type;

    AttachmentType(String type) {
        this.type = type;
    }

    @JsonValue // Java -> JSON 직렬화
    public String getType() {
        return type;
    }

    @JsonCreator // JSON -> Java 역직렬화
    public static AttachmentType from(String value) {
        return Arrays.stream(AttachmentType.values())
                .filter(type -> type.type.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 타입: " + value));
    }
}