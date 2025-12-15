package com.setof.connectly.auth.repository;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.setof.connectly.module.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    @Value(value = "${front.web-domain}")
    private String frontDomainUrl;

    private static final String AUTH_REQUEST = "oauth2-auth-request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    public static final String REFERER_URI_PARAM_COOKIE_NAME = "referer";
    public static final String USER_INTEGRATION_PARAM_COOKIE_NAME = "integration";

    private static final int cookieExpireSeconds = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Optional<Cookie> cookie = CookieUtils.getCookie(request, AUTH_REQUEST);
        return cookie.map(value -> CookieUtils.deserialize(value, OAuth2AuthorizationRequest.class))
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

        CookieUtils.setCookie(
                response,
                AUTH_REQUEST,
                CookieUtils.serialize(authorizationRequest),
                cookieExpireSeconds);
        setRedirectUrlCookie(request, response);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(
            HttpServletRequest request, HttpServletResponse response) {
        removeAuthorizationRequestCookies(response);
        return loadAuthorizationRequest(request);
    }

    public void setRedirectUrlCookie(HttpServletRequest request, HttpServletResponse response) {
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);

        String refererUrl = request.getParameter(REFERER_URI_PARAM_COOKIE_NAME);

        String integration = request.getParameter(USER_INTEGRATION_PARAM_COOKIE_NAME);

        if (StringUtils.isNotBlank(refererUrl)) {
            CookieUtils.setCookie(
                    response, REFERER_URI_PARAM_COOKIE_NAME, refererUrl, cookieExpireSeconds);
        }

        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.setCookie(
                    response,
                    REDIRECT_URI_PARAM_COOKIE_NAME,
                    redirectUriAfterLogin,
                    cookieExpireSeconds);
        } else
            CookieUtils.setCookie(
                    response, REDIRECT_URI_PARAM_COOKIE_NAME, frontDomainUrl, cookieExpireSeconds);

        if (StringUtils.isNotBlank(integration)) {
            CookieUtils.setCookie(
                    response, USER_INTEGRATION_PARAM_COOKIE_NAME, "true", cookieExpireSeconds);
        }
    }

    public void removeAuthorizationRequestCookies(HttpServletResponse response) {
        CookieUtils.deleteCookie(response, AUTH_REQUEST);
        CookieUtils.deleteCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME);
        CookieUtils.deleteCookie(response, REFERER_URI_PARAM_COOKIE_NAME);
        CookieUtils.deleteCookie(response, USER_INTEGRATION_PARAM_COOKIE_NAME);
    }
}
