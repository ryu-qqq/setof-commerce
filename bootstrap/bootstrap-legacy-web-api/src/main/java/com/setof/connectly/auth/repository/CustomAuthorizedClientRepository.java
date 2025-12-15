package com.setof.connectly.auth.repository;

import com.setof.connectly.auth.dto.OAuth2UserInfo;
import com.setof.connectly.module.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthorizedClientRepository implements OAuth2AuthorizedClientRepository {

    private static final String COOKIE_NAME_PREFIX = "oauth2-auth-client-";
    private static final String AUTH_REQUEST = "oauth2-auth-request";

    @Override
    @SuppressWarnings("unchecked")
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(
            String clientRegistrationId, Authentication principal, HttpServletRequest request) {
        Optional<Cookie> cookie =
                CookieUtils.getCookie(request, getCookieName(clientRegistrationId, principal));
        return cookie.map(value -> (T) CookieUtils.deserialize(value)).orElse(null);
    }

    @Override
    public void saveAuthorizedClient(
            OAuth2AuthorizedClient authorizedClient,
            Authentication principal,
            HttpServletRequest request,
            HttpServletResponse response) {
        String cookieName =
                getCookieName(
                        authorizedClient.getClientRegistration().getRegistrationId(), principal);
        CookieUtils.setCookie(
                response, cookieName, CookieUtils.serialize(authorizedClient), 24 * 60 * 60);
        removeAuthorizationRequestCookies(response);
    }

    @Override
    public void removeAuthorizedClient(
            String clientRegistrationId,
            Authentication principal,
            HttpServletRequest request,
            HttpServletResponse response) {
        String cookieName = getCookieName(clientRegistrationId, principal);
        CookieUtils.deleteCookie(response, cookieName);
    }

    private String getCookieName(String clientRegistrationId, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        OAuth2UserInfo userInfo = (OAuth2UserInfo) principal;
        return COOKIE_NAME_PREFIX + clientRegistrationId + "-" + userInfo.getId();
    }

    public void removeAuthorizationRequestCookies(HttpServletResponse response) {
        CookieUtils.deleteCookie(response, AUTH_REQUEST);
    }
}
