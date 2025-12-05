package com.ryuqq.setof.api.auth.controller;

import com.ryuqq.setof.api.auth.component.TokenCookieWriter;
import com.ryuqq.setof.api.auth.security.MemberPrincipal;
import com.ryuqq.setof.api.auth.dto.command.LoginApiRequest;
import com.ryuqq.setof.api.auth.dto.response.TokenApiResponse;
import com.ryuqq.setof.api.auth.mapper.AuthApiMapper;
import com.ryuqq.setof.api.common.dto.ApiResponse;
import com.ryuqq.setof.application.member.dto.command.LocalLoginCommand;
import com.ryuqq.setof.application.member.dto.command.LogoutMemberCommand;
import com.ryuqq.setof.application.member.dto.response.LocalLoginResponse;
import com.ryuqq.setof.application.member.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.port.in.command.LocalLoginUseCase;
import com.ryuqq.setof.application.member.port.in.command.LogoutMemberUseCase;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RestController
@RequestMapping("/api/v1/auth")
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
     * @param request 로그인 요청 (phoneNumber, password)
     * @param response HTTP 응답 (Cookie 설정용)
     * @return TokenApiResponse (토큰 메타정보)
     */
    @PostMapping("/login")
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
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal MemberPrincipal principal, HttpServletResponse response) {

        LogoutMemberCommand command = authApiMapper.toLogoutCommand(principal.getMemberId());
        logoutMemberUseCase.execute(command);

        tokenCookieWriter.deleteTokenCookies(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
