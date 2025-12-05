package com.ryuqq.setof.api.auth.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

/**
 * Cookie Reader Utility
 *
 * <p>JWT 토큰 쿠키 읽기 전용 유틸리티
 *
 * <p>쿠키 쓰기는 {@link com.ryuqq.setof.api.auth.component.TokenCookieWriter} 사용
 *
 * @author development-team
 * @since 1.0.0
 */
public final class CookieUtils {

    public static final String ACCESS_TOKEN_COOKIE = "access_token";
    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    private CookieUtils() {
        // Utility class
    }

    /**
     * Request에서 Access Token 쿠키 추출
     *
     * @param request HttpServletRequest
     * @return Access Token (Optional)
     */
    public static Optional<String> getAccessToken(HttpServletRequest request) {
        return getCookieValue(request, ACCESS_TOKEN_COOKIE);
    }

    /**
     * Request에서 Refresh Token 쿠키 추출
     *
     * @param request HttpServletRequest
     * @return Refresh Token (Optional)
     */
    public static Optional<String> getRefreshToken(HttpServletRequest request) {
        return getCookieValue(request, REFRESH_TOKEN_COOKIE);
    }

    private static Optional<String> getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(value -> value != null && !value.isBlank())
                .findFirst();
    }
}
