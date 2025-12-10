package com.ryuqq.setof.adapter.in.rest.auth.handler;

import com.ryuqq.setof.adapter.in.rest.auth.component.TokenCookieWriter;
import com.ryuqq.setof.adapter.in.rest.auth.config.SecurityProperties;
import com.ryuqq.setof.adapter.in.rest.auth.mapper.KakaoOAuth2ApiMapper;
import com.ryuqq.setof.adapter.in.rest.auth.repository.OAuth2CookieAuthorizationRequestRepository;
import com.ryuqq.setof.adapter.in.rest.auth.utils.CookieUtils;
import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import com.ryuqq.setof.application.member.dto.response.KakaoOAuthResponse;
import com.ryuqq.setof.application.member.port.in.command.KakaoOAuthLoginUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * OAuth2 Success Handler (레거시 리다이렉트 방식)
 *
 * <p>카카오 로그인 성공 후 처리를 담당
 *
 * <p>동작 방식:
 *
 * <ol>
 *   <li>Spring Security OAuth2가 카카오 인증 처리
 *   <li>성공 시 이 핸들러가 호출됨
 *   <li>OAuth2User에서 카카오 사용자 정보 추출
 *   <li>integration 쿠키 확인 → 통합 여부 결정
 *   <li>KakaoOAuthLoginUseCase 호출
 *   <li>토큰을 쿠키에 설정
 *   <li>프론트엔드로 리다이렉트 (joined, referer 쿼리 파라미터 포함)
 * </ol>
 *
 * <p>쿼리 파라미터:
 *
 * <ul>
 *   <li>joined: true (기존 회원) / false (신규 회원 또는 통합됨)
 *   <li>referer: 원래 페이지 URL (있을 경우)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String JOINED_PARAM = "joined";
    private static final String REFERER_PARAM = "referer";

    private final KakaoOAuthLoginUseCase kakaoOAuthLoginUseCase;
    private final KakaoOAuth2ApiMapper kakaoOAuth2ApiMapper;
    private final TokenCookieWriter tokenCookieWriter;
    private final OAuth2CookieAuthorizationRequestRepository authorizationRequestRepository;
    private final SecurityProperties securityProperties;

    public OAuth2SuccessHandler(
            KakaoOAuthLoginUseCase kakaoOAuthLoginUseCase,
            KakaoOAuth2ApiMapper kakaoOAuth2ApiMapper,
            TokenCookieWriter tokenCookieWriter,
            OAuth2CookieAuthorizationRequestRepository authorizationRequestRepository,
            SecurityProperties securityProperties) {
        this.kakaoOAuthLoginUseCase = kakaoOAuthLoginUseCase;
        this.kakaoOAuth2ApiMapper = kakaoOAuth2ApiMapper;
        this.tokenCookieWriter = tokenCookieWriter;
        this.authorizationRequestRepository = authorizationRequestRepository;
        this.securityProperties = securityProperties;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        if (!(authentication instanceof OAuth2AuthenticationToken authToken)) {
            throw new IllegalArgumentException("Authentication must be OAuth2AuthenticationToken");
        }

        OAuth2User oAuth2User = authToken.getPrincipal();

        // integration 쿠키 확인
        boolean integration = CookieUtils.isIntegrationRequest(request);

        // 카카오 사용자 정보 추출 (Mapper 사용)
        KakaoOAuthCommand command =
                kakaoOAuth2ApiMapper.toKakaoOAuthCommand(oAuth2User, integration);

        // UseCase 실행
        KakaoOAuthResponse result = kakaoOAuthLoginUseCase.execute(command);

        // 토큰 쿠키 설정
        tokenCookieWriter.addTokenCookies(
                response,
                result.tokens().accessToken(),
                result.tokens().refreshToken(),
                result.tokens().accessTokenExpiresIn(),
                result.tokens().refreshTokenExpiresIn());

        // 리다이렉트 URL 생성
        String targetUrl = determineTargetUrl(request, result);

        // URL 검증
        if (!isAuthorizedRedirectUri(targetUrl)) {
            throw new IllegalArgumentException("Unauthorized redirect URI: " + targetUrl);
        }

        // 인증 쿠키 정리
        clearAuthenticationAttributes(request, response);

        // 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /**
     * 리다이렉트 URL 결정
     *
     * <p>쿠키에서 redirect_uri 추출 → 없으면 frontDomainUrl 사용
     *
     * <p>쿼리 파라미터:
     *
     * <ul>
     *   <li>joined: 기존 회원 여부 (신규/통합은 false)
     *   <li>referer: 원래 페이지 URL
     * </ul>
     */
    private String determineTargetUrl(HttpServletRequest request, KakaoOAuthResponse result) {
        String baseUrl =
                CookieUtils.getRedirectUri(request)
                        .orElse(securityProperties.getOauth2().getFrontDomainUrl());

        // joined 판단: 신규 회원이거나 통합되었으면 false
        boolean isJoined = !result.isNewMember() && !result.wasIntegrated();

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromUriString(baseUrl).queryParam(JOINED_PARAM, isJoined);

        // referer 추가
        CookieUtils.getReferer(request)
                .ifPresent(
                        referer -> {
                            String encodedReferer =
                                    URLEncoder.encode(referer, StandardCharsets.UTF_8);
                            builder.queryParam(REFERER_PARAM, encodedReferer);
                        });

        return builder.build().toUriString();
    }

    /**
     * 리다이렉트 URI 검증
     *
     * <p>frontDomainUrl과 동일한 호스트/포트인지 확인
     */
    private boolean isAuthorizedRedirectUri(String uri) {
        try {
            URI clientRedirectUri = URI.create(uri);
            URI frontDomainUri = URI.create(securityProperties.getOauth2().getFrontDomainUrl());

            boolean isHostSame =
                    frontDomainUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost());
            boolean isPortSame = frontDomainUri.getPort() == clientRedirectUri.getPort();

            return isHostSame && isPortSame;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 인증 관련 속성 정리
     *
     * <p>세션 속성 + OAuth2 쿠키 삭제
     */
    private void clearAuthenticationAttributes(
            HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(response);
    }
}
