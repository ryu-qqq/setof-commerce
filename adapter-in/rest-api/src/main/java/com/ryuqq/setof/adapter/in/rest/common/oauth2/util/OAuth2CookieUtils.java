package com.ryuqq.setof.adapter.in.rest.common.oauth2.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;
import org.springframework.util.SerializationUtils;

/**
 * OAuth2CookieUtils - OAuth2 인증 흐름 전용 Cookie 유틸리티.
 *
 * <p>OAuth2AuthorizationRequest 직렬화/역직렬화 및 OAuth2 전용 쿠키 관리.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public final class OAuth2CookieUtils {

    private OAuth2CookieUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static void setCookie(
            HttpServletResponse response, String name, String value, int maxAge) {
        response.addHeader(
                "Set-Cookie",
                String.format(
                        "%s=%s; Path=/; Max-Age=%d; HttpOnly; SameSite=Lax",
                        name, value != null ? value : "", maxAge));
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        setCookie(response, name, "", 0);
    }

    @SuppressWarnings("deprecation")
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj));
    }

    @SuppressWarnings("deprecation")
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
