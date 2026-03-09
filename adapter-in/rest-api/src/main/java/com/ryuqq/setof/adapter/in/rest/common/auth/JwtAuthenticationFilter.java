package com.ryuqq.setof.adapter.in.rest.common.auth;

import com.ryuqq.setof.adapter.in.rest.common.util.CookieUtils;
import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheQueryPort;
import com.ryuqq.setof.application.auth.port.out.client.TokenProviderPort;
import com.ryuqq.setof.domain.auth.vo.RefreshTokenCacheKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT 인증 필터.
 *
 * <p>토큰 추출 우선순위:
 *
 * <ol>
 *   <li>Authorization: Bearer {token} 헤더 (신규 방식)
 *   <li>Cookie: token={token} (레거시 호환)
 * </ol>
 *
 * <p>Access Token 만료 시 Refresh Token으로 자동 갱신합니다.
 *
 * <p>Legacy 토큰(sub=userId, role=NORMAL_GRADE)과 New 토큰(sub=memberId, token_type=access) 모두 지원합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String ROLE_CLAIM = "role";
    private static final String REFRESHED_HEADER = "X-Refreshed-Access-Token";

    private final SecretKey secretKey;
    private final CookieUtils cookieUtils;
    private final TokenProviderPort tokenProviderPort;
    private final RefreshTokenCacheQueryPort refreshTokenCacheQueryPort;

    public JwtAuthenticationFilter(
            String secret,
            CookieUtils cookieUtils,
            TokenProviderPort tokenProviderPort,
            RefreshTokenCacheQueryPort refreshTokenCacheQueryPort) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.cookieUtils = cookieUtils;
        this.tokenProviderPort = tokenProviderPort;
        this.refreshTokenCacheQueryPort = refreshTokenCacheQueryPort;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null) {
            try {
                Claims claims = parseClaims(token);
                setAuthentication(claims);
            } catch (ExpiredJwtException e) {
                handleExpiredToken(request, response, e.getClaims());
            } catch (JwtException e) {
                log.debug("Invalid JWT token: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    /** 토큰 추출: Header → Cookie 순서로 시도. */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return cookieUtils.extractAccessToken(request).orElse(null);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    private void setAuthentication(Claims claims) {
        String userId = claims.getSubject();
        String role = claims.get(ROLE_CLAIM, String.class);

        if (StringUtils.hasText(userId)) {
            List<SimpleGrantedAuthority> authorities =
                    StringUtils.hasText(role)
                            ? List.of(new SimpleGrantedAuthority(role))
                            : List.of();

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    /**
     * Access Token 만료 시 Refresh Token으로 자동 갱신.
     *
     * <p>레거시 TokenAuthenticationFilter와 동일한 동작:
     *
     * <ol>
     *   <li>만료된 Access Token의 Claims에서 userId 추출
     *   <li>Cookie에서 Refresh Token 추출
     *   <li>Redis에서 Refresh Token 유효성 검증
     *   <li>새 Access Token 발급 + Cookie 갱신
     *   <li>X-Refreshed-Access-Token: true 헤더 설정
     * </ol>
     */
    private void handleExpiredToken(
            HttpServletRequest request, HttpServletResponse response, Claims expiredClaims) {

        String userId = expiredClaims.getSubject();
        if (!StringUtils.hasText(userId)) {
            return;
        }

        Optional<String> refreshTokenOpt = cookieUtils.extractRefreshToken(request);
        if (refreshTokenOpt.isEmpty()) {
            cookieUtils.deleteTokenCookies(response);
            return;
        }

        String refreshToken = refreshTokenOpt.get();
        RefreshTokenCacheKey cacheKey = RefreshTokenCacheKey.of(refreshToken);
        Optional<String> cachedMemberIdOpt =
                refreshTokenCacheQueryPort.findMemberIdByToken(cacheKey);

        if (cachedMemberIdOpt.isPresent() && userId.equals(cachedMemberIdOpt.get())) {
            TokenPairResponse newTokens = tokenProviderPort.generateTokenPair(userId);
            cookieUtils.setAccessTokenCookie(
                    response, newTokens.accessToken(), (int) newTokens.accessTokenExpiresIn());
            response.setHeader(REFRESHED_HEADER, "true");

            try {
                Claims newClaims = parseClaims(newTokens.accessToken());
                setAuthentication(newClaims);
            } catch (JwtException e) {
                log.warn("Failed to parse newly generated token: {}", e.getMessage());
            }
        } else {
            cookieUtils.deleteTokenCookies(response);
        }
    }
}
