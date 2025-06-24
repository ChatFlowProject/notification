package shop.flowchat.notification.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.flowchat.notification.sse.repository.SseEmitterRepository;

@RestController
@RequestMapping("/sse")
@Tag(name = "SSE 구독 API")
@RequiredArgsConstructor
public class SseController {
    private final SseEmitterRepository emitterRepository;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@RequestParam UUID memberId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitterRepository.add(memberId, emitter);
        return emitter;
    }
}
