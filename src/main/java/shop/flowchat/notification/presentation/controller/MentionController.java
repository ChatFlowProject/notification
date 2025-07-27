package shop.flowchat.notification.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.flowchat.notification.presentation.dto.MentionMessageResponse;
import shop.flowchat.notification.query.MentionQuery;

@RestController
@RequestMapping("/mention")
@Tag(name = "멘션 관련 API")
@RequiredArgsConstructor
public class MentionController {
    private final MentionQuery mentionQuery;

    @Operation(summary = "멘션 목록 조회")
    @GetMapping
    public ResponseEntity<List<MentionMessageResponse>> getMentions(@Parameter(hidden = true) @RequestHeader("Authorization") String token,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "30") int pageSize,
                                                                    @RequestParam(defaultValue = "true") Boolean includeEveryone,
                                                                    @RequestParam(defaultValue = "true") Boolean includeAllTeams,
                                                                    @RequestParam(required = false) UUID teamId) {
        List<MentionMessageResponse> mentions = mentionQuery.findMentionsByMemberId(token, page, pageSize, includeEveryone, includeAllTeams, teamId);
        return ResponseEntity.ok(mentions);
    }
}
