package shop.flowchat.notification.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.flowchat.notification.common.dto.message.MentionMessageResponse;
import shop.flowchat.notification.presentation.dto.CursorResponse;
import shop.flowchat.notification.query.MentionQuery;

@RestController
@RequestMapping("/mention")
@Tag(name = "멘션 관련 API")
@RequiredArgsConstructor
public class MentionController {
    private final MentionQuery mentionQuery;

    @Operation(summary = "멘션 목록 조회 (필터링 포함)")
    @GetMapping
    public ResponseEntity<CursorResponse<MentionMessageResponse>> getMentions(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @RequestParam(required = false) Long nextCursorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime nextCursorCreatedAt,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "true") Boolean includeEveryone,
            @RequestParam(defaultValue = "true") Boolean includeAllTeams,
            @RequestParam(required = false) UUID teamId) {
        return ResponseEntity.ok(mentionQuery.findMentionsByMemberId(
                token, nextCursorId, nextCursorCreatedAt, size, includeEveryone, includeAllTeams, teamId));
    }
}
