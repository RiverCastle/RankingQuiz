package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.dto.response.MemberInfoResponseDto;
import JesusDeciples.RankingQuiz.api.security.SecurityUtil;
import JesusDeciples.RankingQuiz.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
// CORS 설정
public class MemberController {
    private final MemberService memberService;
    @GetMapping("/myInfo")
    public ResponseEntity<MemberInfoResponseDto> findMemberInfoById() {
        return ResponseEntity.ok(memberService.findMemberInfoById(SecurityUtil.getCurrentMemberId()));
    }

    @PutMapping("/myInfo/name")
    public void changeMemberName(@RequestParam("newName") String newName) {
        memberService.chageMemberNameInto(SecurityUtil.getCurrentMemberId(), newName);
    }
}
