package com.ryuqq.setof.application.auth.manager;

import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheQueryPort;
import com.ryuqq.setof.domain.auth.vo.RefreshTokenCacheKey;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * RefreshTokenCacheReadManager - Refresh Token 캐시 조회 Manager.
 *
 * <p>RefreshTokenCacheQueryPort를 래핑합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class RefreshTokenCacheReadManager {

    private final RefreshTokenCacheQueryPort refreshTokenCacheQueryPort;

    public RefreshTokenCacheReadManager(RefreshTokenCacheQueryPort refreshTokenCacheQueryPort) {
        this.refreshTokenCacheQueryPort = refreshTokenCacheQueryPort;
    }

    /**
     * Refresh Token으로 캐시에서 회원 ID를 조회합니다.
     *
     * @param refreshToken Refresh Token
     * @return 회원 ID (Optional)
     */
    public Optional<String> findMemberIdByRefreshToken(String refreshToken) {
        RefreshTokenCacheKey cacheKey = RefreshTokenCacheKey.of(refreshToken);
        return refreshTokenCacheQueryPort.findMemberIdByToken(cacheKey);
    }
}
