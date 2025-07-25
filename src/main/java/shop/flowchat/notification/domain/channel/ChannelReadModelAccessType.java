package shop.flowchat.notification.domain.channel;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ChannelReadModelAccessType {
    PUBLIC, PRIVATE;

    @JsonCreator
    public static ChannelReadModelAccessType of(final String parameter) {
        String type = parameter.toUpperCase();
        return Arrays.stream(values())
                .filter(t -> t.toString().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 채널 접근 유형입니다."));
    }

}
