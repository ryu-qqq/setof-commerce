package com.ryuqq.setof.application.auth.manager;

import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheCommandPort;
import com.ryuqq.setof.domain.auth.vo.RefreshTokenCacheKey;
import org.springframework.stereotype.Component;

/**
 * RefreshTokenCacheCommandManager - Refresh Token 캐시 명령 Manager.
 *
 * <p>RefreshTokenCacheCommandPort를 래핑합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class RefreshTokenCacheCommandManager {

    private final RefreshTokenCacheCommandPort refreshTokenCacheCommandPort;

    public RefreshTokenCacheCommandManager(
            RefreshTokenCacheCommandPort refreshTokenCacheCommandPort) {
        this.refreshTokenCacheCommandPort = refreshTokenCacheCommandPort;
    }

    /**
     * Refresh Token을 캐시에 저장합니다.
     *
     * @param refreshToken Refresh Token
     * @param memberId 회원 ID
     * @param expiresInSeconds 만료 시간 (초)
     */
    public void persist(String refreshToken, String memberId, long expiresInSeconds) {
        RefreshTokenCacheKey cacheKey = RefreshTokenCacheKey.of(refreshToken);
        refreshTokenCacheCommandPort.persist(cacheKey, memberId, expiresInSeconds);
    }

    /**
     * Refresh Token 캐시를 삭제합니다.
     *
     * @param refreshToken Refresh Token
     */
    public void delete(String refreshToken) {
        RefreshTokenCacheKey cacheKey = RefreshTokenCacheKey.of(refreshToken);
        refreshTokenCacheCommandPort.delete(cacheKey);
    }
}
