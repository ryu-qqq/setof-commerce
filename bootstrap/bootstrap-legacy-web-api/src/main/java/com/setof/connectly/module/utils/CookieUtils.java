package com.setof.connectly.module.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

@Component
public class CookieUtils {

    private static final String HEADER_AUTHORIZATION = "token";

    private static String cookieDomain;

    @Value(value = "${front.path}")
    public void setCookieDomain(String domain) {
        CookieUtils.cookieDomain = domain;
    }

    public static void setTokenInCookie(HttpServletResponse response, String token, int maxAge) {
        setCookie(response, HEADER_AUTHORIZATION, token, maxAge);
    }

    public static void setCookie(
            HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);

        if (cookieDomain != null && !cookieDomain.isEmpty()) {
            cookie.setDomain(cookieDomain);
        }

        String domainAttribute =
                (cookieDomain != null && !cookieDomain.isEmpty()) ? "; Domain=" + cookieDomain : "";

        response.addHeader(
                "Set-Cookie",
                String.format(
                        "%s=%s; Path=/; Max-Age=%d; Secure; HttpOnly; SameSite=Lax%s",
                        name, value, maxAge, domainAttribute));
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

    public static void deleteCookie(HttpServletResponse response, String name) {
        setCookie(response, name, null, 0);
    }

    public static String serialize(Object obj) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj));
    }

    public static Object deserialize(Cookie cookie) {
        return SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue()));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
