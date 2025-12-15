package com.setof.connectly.auth.handler;

import com.setof.connectly.auth.repository.CustomAuthorizationRequestRepository;
import com.setof.connectly.auth.repository.CustomAuthorizedClientRepository;
import com.setof.connectly.module.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private final CustomAuthorizedClientRepository customAuthorizedClientRepository;
    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException {
        Optional<Cookie> cookie = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME);

        String targetUrl = "/";
        if (cookie.isPresent()) targetUrl = cookie.get().getValue();

        String redirectUrl = StringUtils.hasText(targetUrl) ? targetUrl : "";
        customAuthorizationRequestRepository.removeAuthorizationRequestCookies(response);
        customAuthorizedClientRepository.removeAuthorizationRequestCookies(response);
        getRedirectStrategy()
                .sendRedirect(request, response, determineRedirectUrl(redirectUrl, exception));
    }

    private String determineRedirectUrl(String targetUrl, AuthenticationException exception) {
        String redirectUrl = StringUtils.hasText(targetUrl) ? targetUrl : "";
        return UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("error", exception.getLocalizedMessage())
                .build()
                .toUriString();
    }
}
