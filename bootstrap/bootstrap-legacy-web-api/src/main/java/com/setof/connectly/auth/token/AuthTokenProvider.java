package com.setof.connectly.auth.token;

import com.setof.connectly.auth.dto.UserPrincipal;
import com.setof.connectly.auth.mapper.TokenMapper;
import com.setof.connectly.module.user.dto.TokenDto;
import com.setof.connectly.module.user.dto.UserDto;
import com.setof.connectly.module.user.entity.Users;
import com.setof.connectly.module.user.enums.UserGradeEnum;
import com.setof.connectly.module.user.redis.RefreshToken;
import com.setof.connectly.module.user.service.token.RefreshTokenRedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public class AuthTokenProvider {
    private final Key key;
    private final RefreshTokenRedisService refreshTokenRedisQueryService;
    private final TokenMapper tokenMapper;

    @Value("${token.ACCESS_TOKEN_EXPIRE_TIME}")
    private long accessTokenExpireTime;

    @Value("${token.REFRESH_TOKEN_EXPIRE_TIME}")
    private long refreshTokenExpireTime;

    public AuthTokenProvider(
            String secret,
            RefreshTokenRedisService refreshTokenRedisQueryService,
            TokenMapper tokenMapper) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.refreshTokenRedisQueryService = refreshTokenRedisQueryService;
        this.tokenMapper = tokenMapper;
    }

    public AuthToken createAuthToken(String id, String role, Date issue, Date expiry) {
        return new AuthToken(id, role, issue, expiry, key);
    }

    public Authentication getAuthentication(UserDto user) {
        UserDetails userPrincipal = UserPrincipal.create(user);
        return new UsernamePasswordAuthenticationToken(
                userPrincipal, user.getPhoneNumber(), userPrincipal.getAuthorities());
    }

    public Authentication getAuthentication(Users user) {
        UserDetails userPrincipal = UserPrincipal.create(user);
        return new UsernamePasswordAuthenticationToken(
                userPrincipal, user.getPhoneNumber(), userPrincipal.getAuthorities());
    }

    public String getUserIdByToken(String token) {
        Claims tokenClaims = getTokenClaims(token);
        assert tokenClaims != null;
        return tokenClaims.getSubject();
    }

    private Claims getTokenClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }

    public TokenDto createToken(String id, UserGradeEnum userGrade) {
        Date now = new Date();
        long currentDateMillis = now.getTime();
        Date accessTokenExpiresIn = new Date(currentDateMillis + accessTokenExpireTime);
        Date refreshTokenExpiresIn = new Date(currentDateMillis + refreshTokenExpireTime);

        String accessToken =
                createAuthToken(id, userGrade.name(), now, accessTokenExpiresIn).getToken();
        String refreshToken =
                createAuthToken(id, userGrade.name(), now, refreshTokenExpiresIn).getToken();

        return tokenMapper.toToken(accessToken, refreshToken);
    }

    public void createRedisRefreshTokenAndSave(
            String username, UserGradeEnum userGrade, String refreshToken) {
        RefreshToken redisRefreshToken =
                tokenMapper.toRefreshToken(
                        username,
                        refreshToken,
                        userGrade.name(),
                        getRemainingMilliSeconds(refreshToken));
        refreshTokenRedisQueryService.saveRefreshToken(redisRefreshToken);
    }

    public long getRemainingMilliSeconds(String token) {
        Date expiration = getTokenClaims(token).getExpiration();
        return expiration.getTime() - (new Date()).getTime();
    }
}
