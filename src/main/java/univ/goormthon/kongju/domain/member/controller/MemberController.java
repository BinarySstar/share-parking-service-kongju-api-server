package univ.goormthon.kongju.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import univ.goormthon.kongju.domain.member.dto.response.ProfileInfo;
import univ.goormthon.kongju.domain.member.service.MemberService;
import univ.goormthon.kongju.global.exception.dto.ErrorResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Tag(name = "Member", description = "회원 정보 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "프로필 조회", description = "로그인한 회원의 프로필을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "프로필 조회 성공", content = @Content(schema = @Schema(implementation = ProfileInfo.class))),
        @ApiResponse(responseCode = "404", description = "회원이 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("sub");
        ProfileInfo profileInfo = memberService.getProfile(email);
        return ResponseEntity.ok(profileInfo);
    }

}