package com.ryuqq.setof.adapter.in.rest.common.oauth2.handler;

import com.ryuqq.setof.adapter.in.rest.common.oauth2.repository.OAuth2AuthorizationRequestCookieRepository;
import com.ryuqq.setof.adapter.in.rest.common.oauth2.util.OAuth2CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * OAuth2AuthenticationFailureHandler - OAuth2 인증 실패 핸들러.
 *
 * <p>인증 실패 시 에러 메시지와 함께 프론트엔드로 리다이렉트합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger log =
            LoggerFactory.getLogger(OAuth2AuthenticationFailureHandler.class);
    private static final String REDIRECT_URI_COOKIE = "redirect_uri";

    @Value(value = "${front.web-domain:http://localhost:3000}")
    private String frontDomainUrl;

    private final OAuth2AuthorizationRequestCookieRepository authorizationRequestRepository;

    public OAuth2AuthenticationFailureHandler(
            OAuth2AuthorizationRequestCookieRepository authorizationRequestRepository) {
        this.authorizationRequestRepository = authorizationRequestRepository;
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException {

        log.error("OAuth2 authentication failed: {}", exception.getMessage());

        String redirectUrl =
                OAuth2CookieUtils.getCookie(request, REDIRECT_URI_COOKIE)
                        .map(Cookie::getValue)
                        .filter(StringUtils::hasText)
                        .orElse(frontDomainUrl);

        String targetUrl =
                UriComponentsBuilder.fromUriString(redirectUrl)
                        .queryParam("error", exception.getLocalizedMessage())
                        .build()
                        .toUriString();

        authorizationRequestRepository.removeAuthorizationRequestCookies(response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
