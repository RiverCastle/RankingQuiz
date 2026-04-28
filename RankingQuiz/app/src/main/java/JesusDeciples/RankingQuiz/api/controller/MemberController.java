package JesusDeciples.RankingQuiz.api.controller;

import JesusDeciples.RankingQuiz.api.dto.response.MemberInfoResponseDto;
import JesusDeciples.RankingQuiz.api.security.SecurityUtil;
import JesusDeciples.RankingQuiz.api.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Tag(name = "Member API", description = "회원 API 입니다.")
// CORS 설정
public class MemberController {
    private final MemberService memberService;
    @GetMapping("/myInfo")
    @Operation(summary = "Member 조회",
            description = "현재 사용자의 회원 정보를 조회하는 API입니다. 이 API는 사용자의 ID를 기반으로 회원 정보를 반환합니다.")
    public ResponseEntity<MemberInfoResponseDto> findMemberInfoById() {
        return ResponseEntity.ok(memberService.findMemberInfoById(SecurityUtil.getCurrentMemberId()));
    }

    @PutMapping("/myInfo/name")
    @Operation(summary = "Member Name 변경",
            description = "회원의 이름을 변경하는 API입니다. 이 API는 KaKao Login API를 통해 회원 가입이 이루어진 후, 초기 설정으로 로컬 파트가 자동으로 지정된 회원의 이름을 업데이트합니다. 새로운 이름은 요청 파라미터로 전달되며, 현재 로그인한 회원의 ID를 기반으로 변경이 수행됩니다.")
    public void changeMemberName(@RequestParam("newName") String newName) {
        memberService.chageMemberNameInto(SecurityUtil.getCurrentMemberId(), newName);
    }
}
