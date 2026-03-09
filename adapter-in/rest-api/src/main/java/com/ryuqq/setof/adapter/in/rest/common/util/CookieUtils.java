package com.ryuqq.setof.adapter.in.rest.common.util;

import com.ryuqq.setof.adapter.in.rest.common.config.CookieProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * HTTP Cookie 관리 유틸리티.
 *
 * <p>JWT Access/Refresh Token을 Cookie로 설정하고 추출합니다. 레거시 프론트엔드와의 호환성을 위해 Cookie 기반 토큰 전달을 지원합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class CookieUtils {

    private static final String ACCESS_TOKEN_COOKIE = "token";
    private static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    private final CookieProperties cookieProperties;

    public CookieUtils(CookieProperties cookieProperties) {
        this.cookieProperties = cookieProperties;
    }

    public void setAccessTokenCookie(HttpServletResponse response, String token, int maxAge) {
        addCookie(response, ACCESS_TOKEN_COOKIE, token, maxAge);
    }

    public void setRefreshTokenCookie(HttpServletResponse response, String token, int maxAge) {
        addCookie(response, REFRESH_TOKEN_COOKIE, token, maxAge);
    }

    public void deleteTokenCookies(HttpServletResponse response) {
        addCookie(response, ACCESS_TOKEN_COOKIE, "", 0);
        addCookie(response, REFRESH_TOKEN_COOKIE, "", 0);
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return extractCookieValue(request, ACCESS_TOKEN_COOKIE);
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return extractCookieValue(request, REFRESH_TOKEN_COOKIE);
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        String domain = cookieProperties.getDomain();
        boolean secure = cookieProperties.isSecure();
        String sameSite = cookieProperties.getSameSite();

        StringBuilder sb = new StringBuilder();
        sb.append(name).append('=').append(value != null ? value : "");
        sb.append("; Path=/");
        sb.append("; Max-Age=").append(maxAge);
        sb.append("; HttpOnly");

        if (secure) {
            sb.append("; Secure");
        }

        if (StringUtils.hasText(sameSite)) {
            sb.append("; SameSite=").append(sameSite);
        }

        if (StringUtils.hasText(domain) && !"localhost".equals(domain)) {
            sb.append("; Domain=").append(domain);
        }

        response.addHeader("Set-Cookie", sb.toString());
    }

    private Optional<String> extractCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                String decoded = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                if (StringUtils.hasText(decoded)) {
                    return Optional.of(decoded);
                }
            }
        }

        return Optional.empty();
    }
}
