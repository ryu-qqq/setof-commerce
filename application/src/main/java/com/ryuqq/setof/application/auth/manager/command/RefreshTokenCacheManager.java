package com.ryuqq.setof.application.auth.manager.command;

import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheCommandPort;
import com.ryuqq.setof.domain.auth.vo.RefreshTokenCacheKey;
import org.springframework.stereotype.Component;

/**
 * Refresh Token Cache Manager
 *
 * <p>Redis에 Refresh Token 캐시 저장/삭제를 관리하는 Manager
 *
 * <p><strong>저장 구조:</strong>
 *
 * <ul>
 *   <li>Key: cache:refresh-token:{token}
 *   <li>Value: memberId (UUID 문자열)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefreshTokenCacheManager {

    private final RefreshTokenCacheCommandPort refreshTokenCacheCommandPort;

    public RefreshTokenCacheManager(RefreshTokenCacheCommandPort refreshTokenCacheCommandPort) {
        this.refreshTokenCacheCommandPort = refreshTokenCacheCommandPort;
    }

    /**
     * Refresh Token 캐시 저장
     *
     * @param tokenValue 토큰 값
     * @param memberId 회원 ID (UUID 문자열)
     * @param expiresInSeconds 만료 시간 (초)
     */
    public void persist(String tokenValue, String memberId, long expiresInSeconds) {
        RefreshTokenCacheKey cacheKey = RefreshTokenCacheKey.of(tokenValue);
        refreshTokenCacheCommandPort.persist(cacheKey, memberId, expiresInSeconds);
    }

    /**
     * Refresh Token 캐시 삭제
     *
     * @param tokenValue 토큰 값
     */
    public void delete(String tokenValue) {
        RefreshTokenCacheKey cacheKey = RefreshTokenCacheKey.of(tokenValue);
        refreshTokenCacheCommandPort.delete(cacheKey);
    }
}
