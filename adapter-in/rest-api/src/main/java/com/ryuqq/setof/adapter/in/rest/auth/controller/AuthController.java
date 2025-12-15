package com.ryuqq.setof.adapter.in.rest.auth.controller;

import com.ryuqq.setof.adapter.in.rest.auth.component.TokenCookieWriter;
import com.ryuqq.setof.adapter.in.rest.auth.dto.command.LoginApiRequest;
import com.ryuqq.setof.adapter.in.rest.auth.dto.response.TokenApiResponse;
import com.ryuqq.setof.adapter.in.rest.auth.mapper.AuthApiMapper;
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.dto.command.LocalLoginCommand;
import com.ryuqq.setof.application.member.dto.command.LogoutMemberCommand;
import com.ryuqq.setof.application.member.dto.response.LocalLoginResponse;
import com.ryuqq.setof.application.member.port.in.command.LocalLoginUseCase;
import com.ryuqq.setof.application.member.port.in.command.LogoutMemberUseCase;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Auth Controller
 *
 * <p>인증 관련 API 엔드포인트 (Local 로그인/로그아웃)
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>Controller는 HTTP 처리만 담당
 *   <li>비즈니스 로직은 UseCase에 위임
 *   <li>JWT 토큰은 HttpOnly Cookie로 전달
 *   <li>Cookie 처리는 TokenCookieWriter에 위임
 *   <li>경로는 ApiV2Paths 상수 사용 (하드코딩 금지)
 * </ul>
 *
 * <p>토큰 갱신 정책:
 *
 * <ul>
 *   <li>Silent Refresh: JwtAuthenticationFilter에서 자동 처리
 *   <li>프론트엔드에서 별도로 refresh 엔드포인트를 호출할 필요 없음
 * </ul>
 *
 * <p>참고: Kakao OAuth2 로그인은 Spring Security OAuth2 프레임워크에서 처리
 *
 * @author development-team
 * @since 1.0.0
 */
@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping(ApiV2Paths.Auth.BASE)
@Validated
public class AuthController {

    private final LocalLoginUseCase localLoginUseCase;
    private final LogoutMemberUseCase logoutMemberUseCase;
    private final AuthApiMapper authApiMapper;
    private final TokenCookieWriter tokenCookieWriter;

    public AuthController(
            LocalLoginUseCase localLoginUseCase,
            LogoutMemberUseCase logoutMemberUseCase,
            AuthApiMapper authApiMapper,
            TokenCookieWriter tokenCookieWriter) {
        this.localLoginUseCase = localLoginUseCase;
        this.logoutMemberUseCase = logoutMemberUseCase;
        this.authApiMapper = authApiMapper;
        this.tokenCookieWriter = tokenCookieWriter;
    }

    /**
     * 로컬 로그인 (핸드폰 번호 + 비밀번호)
     *
     * <p>성공 시 Access Token과 Refresh Token을 HttpOnly Cookie로 설정
     *
     * @param request 로그인 요청 (csPhoneNumber, passwordHash)
     * @param response HTTP 응답 (Cookie 설정용)
     * @return TokenApiResponse (토큰 메타정보)
     */
    @Operation(
            summary = "로그인",
            description = "핸드폰 번호와 비밀번호로 로그인합니다. 성공 시 JWT 토큰이 HttpOnly Cookie로 설정됩니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "로그인 성공",
                        content =
                                @Content(
                                        schema = @Schema(implementation = TokenApiResponse.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 실패",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping(ApiV2Paths.Auth.LOGIN_PATH)
    public ResponseEntity<ApiResponse<TokenApiResponse>> login(
            @Valid @RequestBody LoginApiRequest request, HttpServletResponse response) {

        LocalLoginCommand command = authApiMapper.toLocalLoginCommand(request);
        LocalLoginResponse loginResponse = localLoginUseCase.execute(command);

        TokenPairResponse tokens = loginResponse.tokens();
        tokenCookieWriter.addTokenCookies(
                response,
                tokens.accessToken(),
                tokens.refreshToken(),
                tokens.accessTokenExpiresIn(),
                tokens.refreshTokenExpiresIn());

        TokenApiResponse tokenApiResponse = authApiMapper.toTokenApiResponse(tokens);
        return ResponseEntity.ok(ApiResponse.ofSuccess(tokenApiResponse));
    }

    /**
     * 로그아웃
     *
     * <p>서버에서 Refresh Token 무효화 및 Cookie 삭제
     *
     * @param principal 인증된 사용자 정보
     * @param response HTTP 응답 (Cookie 삭제용)
     * @return 성공 응답
     */
    @Operation(summary = "로그아웃", description = "Refresh Token을 무효화하고 Cookie를 삭제합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "로그아웃 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping(ApiV2Paths.Auth.LOGOUT_PATH)
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal MemberPrincipal principal, HttpServletResponse response) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        LogoutMemberCommand command = authApiMapper.toLogoutCommand(principal.getMemberId());
        logoutMemberUseCase.execute(command);

        tokenCookieWriter.deleteTokenCookies(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
