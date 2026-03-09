package com.ryuqq.setof.adapter.in.rest.common.oauth2.repository;

import com.ryuqq.setof.adapter.in.rest.common.oauth2.util.OAuth2CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * OAuth2AuthorizationRequestCookieRepository - 쿠키 기반 OAuth2 인증 요청 저장소.
 *
 * <p>OAuth2AuthorizationRequest를 쿠키에 직렬화하여 저장합니다. redirect_uri, referer, integration 플래그도 쿠키로
 * 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class OAuth2AuthorizationRequestCookieRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final String AUTH_REQUEST_COOKIE = "oauth2-auth-request";
    static final String REDIRECT_URI_COOKIE = "redirect_uri";
    static final String REFERER_COOKIE = "referer";
    static final String INTEGRATION_COOKIE = "integration";
    private static final int COOKIE_EXPIRE_SECONDS = 180;

    @Value(value = "${front.web-domain:http://localhost:3000}")
    private String frontDomainUrl;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return OAuth2CookieUtils.getCookie(request, AUTH_REQUEST_COOKIE)
                .map(
                        cookie ->
                                OAuth2CookieUtils.deserialize(
                                        cookie, OAuth2AuthorizationRequest.class))
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

        OAuth2CookieUtils.setCookie(
                response,
                AUTH_REQUEST_COOKIE,
                OAuth2CookieUtils.serialize(authorizationRequest),
                COOKIE_EXPIRE_SECONDS);
        saveRedirectCookies(request, response);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(
            HttpServletRequest request, HttpServletResponse response) {
        OAuth2AuthorizationRequest authRequest = loadAuthorizationRequest(request);
        removeAuthorizationRequestCookies(response);
        return authRequest;
    }

    public void removeAuthorizationRequestCookies(HttpServletResponse response) {
        OAuth2CookieUtils.deleteCookie(response, AUTH_REQUEST_COOKIE);
        OAuth2CookieUtils.deleteCookie(response, REDIRECT_URI_COOKIE);
        OAuth2CookieUtils.deleteCookie(response, REFERER_COOKIE);
        OAuth2CookieUtils.deleteCookie(response, INTEGRATION_COOKIE);
    }

    private void saveRedirectCookies(HttpServletRequest request, HttpServletResponse response) {
        String redirectUri = request.getParameter(REDIRECT_URI_COOKIE);
        String referer = request.getParameter(REFERER_COOKIE);
        String integration = request.getParameter(INTEGRATION_COOKIE);

        if (StringUtils.hasText(referer)) {
            OAuth2CookieUtils.setCookie(response, REFERER_COOKIE, referer, COOKIE_EXPIRE_SECONDS);
        }

        if (StringUtils.hasText(redirectUri)) {
            OAuth2CookieUtils.setCookie(
                    response, REDIRECT_URI_COOKIE, redirectUri, COOKIE_EXPIRE_SECONDS);
        } else {
            OAuth2CookieUtils.setCookie(
                    response, REDIRECT_URI_COOKIE, frontDomainUrl, COOKIE_EXPIRE_SECONDS);
        }

        if (StringUtils.hasText(integration)) {
            OAuth2CookieUtils.setCookie(
                    response, INTEGRATION_COOKIE, "true", COOKIE_EXPIRE_SECONDS);
        }
    }
}
