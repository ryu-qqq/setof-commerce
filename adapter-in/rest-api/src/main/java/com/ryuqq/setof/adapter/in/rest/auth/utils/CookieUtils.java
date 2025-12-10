package com.ryuqq.setof.adapter.in.rest.auth.utils;

import com.ryuqq.setof.adapter.in.rest.auth.component.TokenCookieWriter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

/**
 * Cookie Utility
 *
 * <p>JWT 토큰 및 OAuth2 관련 쿠키 유틸리티
 *
 * <p>JWT 토큰 쿠키 쓰기는 {@link TokenCookieWriter} 사용
 *
 * @author development-team
 * @since 1.0.0
 */
public final class CookieUtils {

    public static final String ACCESS_TOKEN_COOKIE = "access_token";
    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    /** OAuth2 관련 쿠키 이름 */
    public static final String REDIRECT_URI_COOKIE = "redirect_uri";

    public static final String REFERER_COOKIE = "referer";
    public static final String INTEGRATION_COOKIE = "integration";

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

    /**
     * Request에서 쿠키 객체 추출
     *
     * @param request HttpServletRequest
     * @param name 쿠키 이름
     * @return Cookie (Optional)
     */
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies).filter(cookie -> name.equals(cookie.getName())).findFirst();
    }

    /**
     * 쿠키 추가
     *
     * @param response HttpServletResponse
     * @param name 쿠키 이름
     * @param value 쿠키 값
     * @param maxAge 만료 시간 (초)
     */
    public static void addCookie(
            HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 쿠키 삭제
     *
     * @param response HttpServletResponse
     * @param name 쿠키 이름
     */
    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /**
     * Request에서 Redirect URI 쿠키 추출
     *
     * @param request HttpServletRequest
     * @return Redirect URI (Optional)
     */
    public static Optional<String> getRedirectUri(HttpServletRequest request) {
        return getCookieValue(request, REDIRECT_URI_COOKIE);
    }

    /**
     * Request에서 Referer 쿠키 추출
     *
     * @param request HttpServletRequest
     * @return Referer (Optional)
     */
    public static Optional<String> getReferer(HttpServletRequest request) {
        return getCookieValue(request, REFERER_COOKIE);
    }

    /**
     * Request에서 Integration 쿠키 추출
     *
     * @param request HttpServletRequest
     * @return Integration 여부
     */
    public static boolean isIntegrationRequest(HttpServletRequest request) {
        return getCookieValue(request, INTEGRATION_COOKIE).map("true"::equals).orElse(false);
    }
}
