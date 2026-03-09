package com.ryuqq.setof.adapter.in.rest.common.oauth2.handler;

import com.ryuqq.setof.adapter.in.rest.common.oauth2.dto.OAuth2UserInfo;
import com.ryuqq.setof.adapter.in.rest.common.oauth2.repository.OAuth2AuthorizationRequestCookieRepository;
import com.ryuqq.setof.adapter.in.rest.common.oauth2.util.OAuth2CookieUtils;
import com.ryuqq.setof.adapter.in.rest.common.util.CookieUtils;
import com.ryuqq.setof.application.auth.dto.response.KakaoLoginResult;
import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import com.ryuqq.setof.application.member.dto.command.KakaoLoginCommand;
import com.ryuqq.setof.application.member.port.in.KakaoLoginUseCase;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * OAuth2AuthenticationSuccessHandler - OAuth2 인증 성공 핸들러.
 *
 * <p>OAuth2 인증 성공 시 OAuth2UserInfo → KakaoLoginCommand 변환 후 KakaoLoginUseCase에 위임합니다. 레거시
 * SuccessHandler의 회원 조회/생성/통합 로직이 전부 KakaoLoginUseCase → KakaoLoginService → KakaoLoginCoordinator로
 * 이동하여 이 핸들러는 얇게 유지됩니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String REDIRECT_URI_COOKIE = "redirect_uri";
    private static final String REFERER_COOKIE = "referer";
    private static final String INTEGRATION_COOKIE = "integration";
    private static final String TRUE = "true";
    private static final int ACCESS_TOKEN_MAX_AGE = 3600;
    private static final int REFRESH_TOKEN_MAX_AGE = 604800;

    @Value(value = "${front.web-domain:http://localhost:3000}")
    private String frontDomainUrl;

    @Value(value = "${front.stage-domain:}")
    private String stageDomainUrl;

    private final KakaoLoginUseCase kakaoLoginUseCase;
    private final CookieUtils cookieUtils;
    private final OAuth2AuthorizationRequestCookieRepository authorizationRequestRepository;

    public OAuth2AuthenticationSuccessHandler(
            KakaoLoginUseCase kakaoLoginUseCase,
            CookieUtils cookieUtils,
            OAuth2AuthorizationRequestCookieRepository authorizationRequestRepository) {
        this.kakaoLoginUseCase = kakaoLoginUseCase;
        this.cookieUtils = cookieUtils;
        this.authorizationRequestRepository = authorizationRequestRepository;
    }

    @Override
    protected void handle(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        String targetUrl = resolveTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response already committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String resolveTargetUrl(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {

        OAuth2UserInfo userInfo = extractOAuth2UserInfo(authentication);
        boolean integration = isIntegrationRequest(request);

        KakaoLoginCommand command =
                new KakaoLoginCommand(
                        userInfo.getPhoneNumber(),
                        userInfo.getName(),
                        userInfo.getEmail(),
                        userInfo.getId(),
                        userInfo.getGender(),
                        userInfo.getDateOfBirth(),
                        integration);

        KakaoLoginResult result = kakaoLoginUseCase.execute(command);
        LoginResult loginResult = result.loginResult();

        cookieUtils.setAccessTokenCookie(response, loginResult.accessToken(), ACCESS_TOKEN_MAX_AGE);
        cookieUtils.setRefreshTokenCookie(
                response, loginResult.refreshToken(), REFRESH_TOKEN_MAX_AGE);

        String redirectUrl = resolveRedirectUrl(request);
        String targetUrl =
                UriComponentsBuilder.fromUriString(redirectUrl)
                        .queryParam("joined", result.joined())
                        .build()
                        .toUriString();

        if (!isAuthorizedRedirectUri(targetUrl)) {
            return frontDomainUrl + "?joined=" + result.joined();
        }

        Optional<Cookie> refererCookie = OAuth2CookieUtils.getCookie(request, REFERER_COOKIE);
        if (refererCookie.isPresent() && StringUtils.hasText(refererCookie.get().getValue())) {
            targetUrl += "&referer=" + refererCookie.get().getValue();
        }

        return targetUrl;
    }

    private OAuth2UserInfo extractOAuth2UserInfo(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuth2UserInfo oAuth2UserInfo) {
            return oAuth2UserInfo;
        }
        throw new ClassCastException("Authentication principal을 OAuth2UserInfo로 변환할 수 없습니다.");
    }

    private boolean isIntegrationRequest(HttpServletRequest request) {
        return OAuth2CookieUtils.getCookie(request, INTEGRATION_COOKIE)
                .map(cookie -> TRUE.equals(cookie.getValue()))
                .orElse(false);
    }

    private String resolveRedirectUrl(HttpServletRequest request) {
        return OAuth2CookieUtils.getCookie(request, REDIRECT_URI_COOKIE)
                .map(Cookie::getValue)
                .filter(StringUtils::hasText)
                .orElse(frontDomainUrl);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientUri = URI.create(uri);
        if (isMatchingDomain(clientUri, frontDomainUrl)) {
            return true;
        }
        return StringUtils.hasText(stageDomainUrl) && isMatchingDomain(clientUri, stageDomainUrl);
    }

    private boolean isMatchingDomain(URI clientUri, String allowedUrl) {
        if (!StringUtils.hasText(allowedUrl)) {
            return false;
        }
        URI allowedUri = URI.create(allowedUrl);
        return allowedUri.getHost().equalsIgnoreCase(clientUri.getHost())
                && allowedUri.getPort() == clientUri.getPort();
    }
}
