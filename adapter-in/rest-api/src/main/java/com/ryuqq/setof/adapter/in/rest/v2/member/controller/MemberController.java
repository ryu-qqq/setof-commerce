package com.ryuqq.setof.adapter.in.rest.v2.member.controller;

import com.ryuqq.setof.adapter.in.rest.auth.component.TokenCookieWriter;
import com.ryuqq.setof.adapter.in.rest.auth.dto.response.TokenApiResponse;
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.member.dto.command.KakaoLinkApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.member.dto.command.RegisterMemberApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.member.dto.command.ResetPasswordApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.member.dto.command.WithdrawApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.member.dto.response.MemberApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.member.mapper.MemberApiMapper;
import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.dto.command.IntegrateKakaoCommand;
import com.ryuqq.setof.application.member.dto.command.RegisterMemberCommand;
import com.ryuqq.setof.application.member.dto.command.ResetPasswordCommand;
import com.ryuqq.setof.application.member.dto.command.WithdrawMemberCommand;
import com.ryuqq.setof.application.member.dto.query.GetCurrentMemberQuery;
import com.ryuqq.setof.application.member.dto.response.MemberDetailResponse;
import com.ryuqq.setof.application.member.dto.response.RegisterMemberResponse;
import com.ryuqq.setof.application.member.port.in.command.IntegrateKakaoUseCase;
import com.ryuqq.setof.application.member.port.in.command.RegisterMemberUseCase;
import com.ryuqq.setof.application.member.port.in.command.ResetPasswordUseCase;
import com.ryuqq.setof.application.member.port.in.command.WithdrawMemberUseCase;
import com.ryuqq.setof.application.member.port.in.query.GetCurrentMemberUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Member Controller
 *
 * <p>회원 관련 API 엔드포인트 (회원가입/내정보/탈퇴/비밀번호재설정/카카오연동)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>Controller는 HTTP 처리만 담당
 *   <li>비즈니스 로직은 UseCase에 위임
 *   <li>Command/Query 분리 (CQRS)
 *   <li>경로는 ApiV2Paths 상수 사용 (하드코딩 금지)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Tag(name = "Member", description = "회원 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Members.BASE)
@Validated
public class MemberController {

    private final RegisterMemberUseCase registerMemberUseCase;
    private final GetCurrentMemberUseCase getCurrentMemberUseCase;
    private final WithdrawMemberUseCase withdrawMemberUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final IntegrateKakaoUseCase integrateKakaoUseCase;
    private final MemberApiMapper memberApiMapper;
    private final TokenCookieWriter tokenCookieWriter;

    public MemberController(
            RegisterMemberUseCase registerMemberUseCase,
            GetCurrentMemberUseCase getCurrentMemberUseCase,
            WithdrawMemberUseCase withdrawMemberUseCase,
            ResetPasswordUseCase resetPasswordUseCase,
            IntegrateKakaoUseCase integrateKakaoUseCase,
            MemberApiMapper memberApiMapper,
            TokenCookieWriter tokenCookieWriter) {
        this.registerMemberUseCase = registerMemberUseCase;
        this.getCurrentMemberUseCase = getCurrentMemberUseCase;
        this.withdrawMemberUseCase = withdrawMemberUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
        this.integrateKakaoUseCase = integrateKakaoUseCase;
        this.memberApiMapper = memberApiMapper;
        this.tokenCookieWriter = tokenCookieWriter;
    }

    /**
     * 회원가입 (Local Provider)
     *
     * <p>성공 시 Access Token과 Refresh Token을 HttpOnly Cookie로 설정
     *
     * @param request 회원가입 요청
     * @param response HTTP 응답 (Cookie 설정용)
     * @return TokenApiResponse (토큰 메타정보)
     */
    @Operation(
            summary = "회원가입",
            description =
                    "새로운 회원을 등록합니다. 성공 시 Access Token과 Refresh Token을 HttpOnly Cookie로 설정합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "201",
                        description = "회원가입 성공",
                        content =
                                @Content(
                                        schema = @Schema(implementation = TokenApiResponse.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "중복된 핸드폰 번호",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping
    public ResponseEntity<ApiResponse<TokenApiResponse>> register(
            @Valid @RequestBody RegisterMemberApiRequest request, HttpServletResponse response) {

        RegisterMemberCommand command = memberApiMapper.toRegisterCommand(request);
        RegisterMemberResponse registerResponse = registerMemberUseCase.execute(command);

        TokenPairResponse tokens = registerResponse.tokens();
        setTokenCookies(response, tokens);

        TokenApiResponse tokenApiResponse = TokenApiResponse.from(tokens);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ofSuccess(tokenApiResponse));
    }

    /**
     * 내 정보 조회
     *
     * @param principal 인증된 사용자 정보
     * @return MemberApiResponse (회원 상세 정보)
     */
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 회원의 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공",
                        content =
                                @Content(
                                        schema =
                                                @Schema(implementation = MemberApiResponse.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "회원 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Members.ME_PATH)
    public ResponseEntity<ApiResponse<MemberApiResponse>> getCurrentMember(
            @AuthenticationPrincipal MemberPrincipal principal) {

        GetCurrentMemberQuery query =
                memberApiMapper.toGetCurrentMemberQuery(principal.getMemberId());
        MemberDetailResponse memberDetail = getCurrentMemberUseCase.execute(query);

        MemberApiResponse memberApiResponse = MemberApiResponse.from(memberDetail);
        return ResponseEntity.ok(ApiResponse.ofSuccess(memberApiResponse));
    }

    /**
     * 회원 탈퇴 (Soft Delete)
     *
     * <p>상태를 WITHDRAWN으로 변경하고 Token Cookie 삭제
     *
     * <p>컨벤션: DELETE 메서드 금지, 소프트 삭제는 PATCH로 처리
     *
     * @param principal 인증된 사용자 정보
     * @param request 탈퇴 요청 (탈퇴 사유)
     * @param response HTTP 응답 (Cookie 삭제용)
     * @return 성공 응답
     */
    @Operation(
            summary = "회원 탈퇴",
            description = "회원 상태를 WITHDRAWN으로 변경합니다 (소프트 삭제). Token Cookie가 삭제됩니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "탈퇴 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "403",
                        description = "이미 탈퇴한 회원",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.Members.ME_PATH + ApiV2Paths.Members.WITHDRAW_PATH)
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody WithdrawApiRequest request,
            HttpServletResponse response) {

        WithdrawMemberCommand command =
                memberApiMapper.toWithdrawCommand(principal.getMemberId(), request);
        withdrawMemberUseCase.execute(command);

        clearTokenCookies(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * 비밀번호 재설정 (핸드폰 번호 기반)
     *
     * <p>사전에 SMS 인증 완료 필요 (별도 API)
     *
     * @param request 비밀번호 재설정 요청
     * @return 성공 응답
     */
    @Operation(
            summary = "비밀번호 재설정",
            description = "핸드폰 번호 기반으로 비밀번호를 재설정합니다. 사전에 SMS 인증이 완료되어야 합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "재설정 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "비밀번호 정책 위반",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "회원 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping(ApiV2Paths.Members.PASSWORD_RESET_PATH)
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordApiRequest request) {

        ResetPasswordCommand command = memberApiMapper.toResetPasswordCommand(request);
        resetPasswordUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * 카카오 계정 연동 (기존 Local 회원)
     *
     * <p>Local 회원이 카카오 계정을 연동할 때 사용
     *
     * @param principal 인증된 사용자 정보
     * @param request 카카오 연동 요청 (kakaoId)
     * @return 성공 응답
     */
    @Operation(summary = "카카오 계정 연동", description = "기존 Local 회원이 카카오 계정을 연동합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "연동 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "이미 카카오 연동됨",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping(ApiV2Paths.Members.ME_PATH + ApiV2Paths.Members.KAKAO_LINK_PATH)
    public ResponseEntity<ApiResponse<Void>> linkKakao(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody KakaoLinkApiRequest request) {

        IntegrateKakaoCommand command =
                memberApiMapper.toIntegrateKakaoCommand(principal.getMemberId(), request);
        integrateKakaoUseCase.execute(command);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /** 토큰 Cookie 설정 */
    private void setTokenCookies(HttpServletResponse response, TokenPairResponse tokens) {
        tokenCookieWriter.addTokenCookies(
                response,
                tokens.accessToken(),
                tokens.refreshToken(),
                tokens.accessTokenExpiresIn(),
                tokens.refreshTokenExpiresIn());
    }

    /** 토큰 Cookie 삭제 */
    private void clearTokenCookies(HttpServletResponse response) {
        tokenCookieWriter.deleteTokenCookies(response);
    }
}
