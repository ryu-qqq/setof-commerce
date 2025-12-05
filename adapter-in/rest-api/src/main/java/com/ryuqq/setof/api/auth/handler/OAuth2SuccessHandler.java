package com.ryuqq.setof.api.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.setof.api.auth.component.TokenCookieWriter;
import com.ryuqq.setof.api.auth.dto.response.TokenApiResponse;
import com.ryuqq.setof.api.auth.mapper.KakaoOAuth2Mapper;
import com.ryuqq.setof.api.common.dto.ApiResponse;
import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import com.ryuqq.setof.application.member.dto.response.KakaoOAuthResponse;
import com.ryuqq.setof.application.member.port.in.command.KakaoOAuthLoginUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * OAuth2 Success Handler
 *
 * <p>카카오 로그인 성공 후 처리를 담당
 *
 * <p>동작 방식:
 *
 * <ol>
 *   <li>Spring Security OAuth2가 카카오 인증 처리
 *   <li>성공 시 이 핸들러가 호출됨
 *   <li>OAuth2User에서 카카오 사용자 정보 추출
 *   <li>KakaoOAuthLoginUseCase 호출
 *   <li>토큰을 쿠키에 설정하고 JSON 응답 반환
 * </ol>
 *
 * <p>시나리오:
 *
 * <ul>
 *   <li>신규 카카오 회원: 자동 회원가입 + 토큰 발급
 *   <li>기존 카카오 회원: 토큰 발급
 *   <li>기존 LOCAL 회원 (동일 핸드폰): 통합 유도 플래그
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final KakaoOAuthLoginUseCase kakaoOAuthLoginUseCase;
    private final KakaoOAuth2Mapper kakaoOAuth2Mapper;
    private final TokenCookieWriter tokenCookieWriter;
    private final ObjectMapper objectMapper;

    public OAuth2SuccessHandler(
            KakaoOAuthLoginUseCase kakaoOAuthLoginUseCase,
            KakaoOAuth2Mapper kakaoOAuth2Mapper,
            TokenCookieWriter tokenCookieWriter,
            ObjectMapper objectMapper) {
        this.kakaoOAuthLoginUseCase = kakaoOAuthLoginUseCase;
        this.kakaoOAuth2Mapper = kakaoOAuth2Mapper;
        this.tokenCookieWriter = tokenCookieWriter;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        if (!(authentication instanceof OAuth2AuthenticationToken authToken)) {
            throw new IllegalArgumentException("Authentication must be OAuth2AuthenticationToken");
        }
        OAuth2User oAuth2User = authToken.getPrincipal();

        // 카카오 사용자 정보 추출 (Mapper 사용)
        KakaoOAuthCommand command = kakaoOAuth2Mapper.toKakaoOAuthCommand(oAuth2User);

        // UseCase 실행
        KakaoOAuthResponse result = kakaoOAuthLoginUseCase.execute(command);

        // 응답 처리
        if (result.needsIntegration()) {
            // LOCAL 회원 통합 필요 - 토큰 없이 플래그만 반환
            writeJsonResponse(
                    response,
                    ApiResponse.ofSuccess(TokenApiResponse.needsIntegration(result.memberId())));
        } else {
            // 토큰 발급 성공 - 쿠키 설정 + 응답
            tokenCookieWriter.addTokenCookies(
                    response,
                    result.tokens().accessToken(),
                    result.tokens().refreshToken(),
                    result.tokens().accessTokenExpiresIn(),
                    result.tokens().refreshTokenExpiresIn());

            writeJsonResponse(
                    response,
                    ApiResponse.ofSuccess(
                            TokenApiResponse.of(
                                    result.tokens().accessToken(),
                                    result.tokens().accessTokenExpiresIn(),
                                    result.isNewMember())));
        }
    }

    private void writeJsonResponse(HttpServletResponse response, ApiResponse<?> apiResponse)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
