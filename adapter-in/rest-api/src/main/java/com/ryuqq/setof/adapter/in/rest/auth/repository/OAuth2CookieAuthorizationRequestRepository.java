package com.ryuqq.setof.adapter.in.rest.auth.repository;

import com.ryuqq.setof.adapter.in.rest.auth.config.SecurityProperties;
import com.ryuqq.setof.adapter.in.rest.auth.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Base64;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

/**
 * OAuth2 Cookie Authorization Request Repository
 *
 * <p>OAuth2 인증 요청을 쿠키에 저장하고 관리
 *
 * <p>저장되는 쿠키:
 *
 * <ul>
 *   <li>oauth2_auth_request: OAuth2 인증 요청 객체 (직렬화)
 *   <li>redirect_uri: 인증 후 리다이렉트할 프론트엔드 URL
 *   <li>referer: 원래 페이지 URL
 *   <li>integration: 카카오 계정 통합 여부
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class OAuth2CookieAuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE = "oauth2_auth_request";

    private final int cookieExpireSeconds;

    public OAuth2CookieAuthorizationRequestRepository(SecurityProperties securityProperties) {
        this.cookieExpireSeconds = securityProperties.getOauth2().getCookieExpireSeconds();
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE)
                .map(cookie -> deserialize(cookie.getValue()))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(
            OAuth2AuthorizationRequest authorizationRequest,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(response);
            return;
        }

        String serialized = serialize(authorizationRequest);
        CookieUtils.addCookie(
                response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE, serialized, cookieExpireSeconds);

        String redirectUri = request.getParameter(CookieUtils.REDIRECT_URI_COOKIE);
        if (redirectUri != null && !redirectUri.isBlank()) {
            CookieUtils.addCookie(
                    response, CookieUtils.REDIRECT_URI_COOKIE, redirectUri, cookieExpireSeconds);
        }

        String referer = request.getParameter(CookieUtils.REFERER_COOKIE);
        if (referer != null && !referer.isBlank()) {
            CookieUtils.addCookie(
                    response, CookieUtils.REFERER_COOKIE, referer, cookieExpireSeconds);
        }

        String integration = request.getParameter(CookieUtils.INTEGRATION_COOKIE);
        if (integration != null && !integration.isBlank()) {
            CookieUtils.addCookie(
                    response, CookieUtils.INTEGRATION_COOKIE, integration, cookieExpireSeconds);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(
            HttpServletRequest request, HttpServletResponse response) {

        OAuth2AuthorizationRequest authRequest = loadAuthorizationRequest(request);
        if (authRequest != null) {
            removeAuthorizationRequestCookies(response);
        }
        return authRequest;
    }

    /**
     * OAuth2 인증 관련 쿠키 삭제
     *
     * @param response HttpServletResponse
     */
    public void removeAuthorizationRequestCookies(HttpServletResponse response) {
        CookieUtils.deleteCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE);
        CookieUtils.deleteCookie(response, CookieUtils.REDIRECT_URI_COOKIE);
        CookieUtils.deleteCookie(response, CookieUtils.REFERER_COOKIE);
        CookieUtils.deleteCookie(response, CookieUtils.INTEGRATION_COOKIE);
    }

    private String serialize(OAuth2AuthorizationRequest authorizationRequest) {
        byte[] bytes = SerializationUtils.serialize(authorizationRequest);
        return Base64.getUrlEncoder().encodeToString(bytes);
    }

    private OAuth2AuthorizationRequest deserialize(String encoded) {
        byte[] bytes = Base64.getUrlDecoder().decode(encoded);
        return (OAuth2AuthorizationRequest) SerializationUtils.deserialize(bytes);
    }
}
