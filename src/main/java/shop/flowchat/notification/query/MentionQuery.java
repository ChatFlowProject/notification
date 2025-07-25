package shop.flowchat.notification.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentionQuery {


//    public List<MentionMessageResponse> findMentionMessagesByMemberId(UUID memberId) {
//        List<MentionMember> mentionMembers = mentionMemberRepository.findAllByMemberId(memberId);
//
//        return mentionMembers.stream()
//                .map(mm -> {
//                    Mention mention = mm.getMention();
//                    Message message = messageRepository.findById(mention.getMessageId())
//                            .orElseThrow(...); // 존재하지 않으면 예외
//                    return MentionMessageResponse.from(message);
//                })
//                .toList();
//    }
}
