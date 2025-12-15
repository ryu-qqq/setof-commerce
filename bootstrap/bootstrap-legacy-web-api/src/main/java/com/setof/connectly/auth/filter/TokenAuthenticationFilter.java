package com.setof.connectly.auth.filter;

import com.setof.connectly.auth.token.AuthTokenProvider;
import com.setof.connectly.module.user.dto.TokenDto;
import com.setof.connectly.module.user.dto.UserDto;
import com.setof.connectly.module.user.enums.UserGradeEnum;
import com.setof.connectly.module.user.redis.RefreshToken;
import com.setof.connectly.module.user.service.fetch.UserFindServiceImpl;
import com.setof.connectly.module.user.service.token.RefreshTokenRedisService;
import com.setof.connectly.module.utils.CookieUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthTokenProvider tokenProvider;
    private static final String ACCESS_TOKEN = "token";
    private static final String REFRESH_TOKEN = "refresh_token";

    private final UserFindServiceImpl userFindService;
    private final RefreshTokenRedisService refreshTokenRedisService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String tokenStr = getAccessToken(request);

        try {
            if (StringUtils.hasText(tokenStr)) {
                setAuthentication(tokenStr);
            }
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            String id = claims.getSubject();
            handleExpiredToken(request, response, id, filterChain);
        }
    }

    private String getAccessToken(HttpServletRequest request) throws UnsupportedEncodingException {
        Optional<Cookie> cookie = CookieUtils.getCookie(request, ACCESS_TOKEN);

        if (cookie.isPresent()) {
            String decodedValue =
                    URLDecoder.decode(cookie.get().getValue(), StandardCharsets.UTF_8.toString());
            if (StringUtils.hasText(decodedValue)) {
                return decodedValue;
            }
        }

        return null;
    }

    private String getRefreshToken(HttpServletRequest request) throws UnsupportedEncodingException {
        Optional<Cookie> cookie = CookieUtils.getCookie(request, REFRESH_TOKEN);

        if (cookie.isPresent()) {
            String decodedValue =
                    URLDecoder.decode(cookie.get().getValue(), StandardCharsets.UTF_8.toString());
            if (StringUtils.hasText(decodedValue)) {
                return decodedValue;
            }
        }

        return null;
    }

    private void setAuthentication(String token) {
        String userIdByToken = tokenProvider.getUserIdByToken(token);
        UserDto user = getUser(userIdByToken);
        Authentication authentication = tokenProvider.getAuthentication(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        MDC.put("user", String.valueOf(user.getUserId()));
    }

    private UserDto getUser(String token) {
        return userFindService.fetchUser(Long.parseLong(token));
    }

    private void handleExpiredToken(
            HttpServletRequest request,
            HttpServletResponse response,
            String userId,
            FilterChain filterChain)
            throws IOException, ServletException {

        String requestRefreshToken = getRefreshToken(request);
        if (StringUtils.hasText(requestRefreshToken)) {
            Optional<RefreshToken> refreshTokenOpt = refreshTokenRedisService.findByUserId(userId);
            if (refreshTokenOpt.isPresent()
                    && requestRefreshToken.equals(refreshTokenOpt.get().getRefreshToken())) {
                TokenDto token =
                        tokenProvider.createToken(
                                refreshTokenOpt.get().getId(),
                                UserGradeEnum.of(refreshTokenOpt.get().getUserGrade()));
                CookieUtils.setTokenInCookie(response, token.getAccessToken(), 3600);
                response.setHeader("X-Refreshed-Access-Token", "true");
                setAuthentication(token.getAccessToken());
            } else {
                CookieUtils.deleteCookie(response, ACCESS_TOKEN);
                CookieUtils.deleteCookie(response, REFRESH_TOKEN);
            }
        } else {
            // 액세스 토큰만 만료된 경우, 쿠키 삭제
            CookieUtils.deleteCookie(response, ACCESS_TOKEN);
        }

        filterChain.doFilter(request, response);
    }
}
