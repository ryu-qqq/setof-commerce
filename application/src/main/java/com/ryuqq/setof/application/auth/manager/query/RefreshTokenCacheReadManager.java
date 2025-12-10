package com.ryuqq.setof.application.auth.manager.query;

import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheQueryPort;
import com.ryuqq.setof.domain.auth.vo.RefreshTokenCacheKey;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Refresh Token Cache Read Manager
 *
 * <p>Redis에서 Refresh Token 캐시 조회를 관리하는 Manager
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefreshTokenCacheReadManager {

    private final RefreshTokenCacheQueryPort refreshTokenCacheQueryPort;

    public RefreshTokenCacheReadManager(RefreshTokenCacheQueryPort refreshTokenCacheQueryPort) {
        this.refreshTokenCacheQueryPort = refreshTokenCacheQueryPort;
    }

    /**
     * Refresh Token으로 회원 ID 조회
     *
     * @param tokenValue 토큰 값
     * @return 회원 ID (Optional)
     */
    public Optional<String> findMemberIdByToken(String tokenValue) {
        RefreshTokenCacheKey cacheKey = RefreshTokenCacheKey.of(tokenValue);
        return refreshTokenCacheQueryPort.findMemberIdByToken(cacheKey);
    }
}
