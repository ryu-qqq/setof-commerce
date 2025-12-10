package com.ryuqq.setof.application.auth.port.out.cache;

import com.ryuqq.setof.domain.auth.vo.RefreshTokenCacheKey;
import java.util.Optional;

/**
 * Refresh Token Cache Query Port
 *
 * <p>Refresh Token 캐시(Redis) 조회를 담당하는 Port-Out 인터페이스
 *
 * <p><strong>조회 구조:</strong>
 *
 * <ul>
 *   <li>Key: cache:refresh-token:{token}
 *   <li>Value: memberId (UUID 문자열)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RefreshTokenCacheQueryPort {

    /**
     * Refresh Token으로 회원 ID 조회
     *
     * @param cacheKey 캐시 키 (token 기반)
     * @return 회원 ID (UUID 문자열, Optional)
     */
    Optional<String> findMemberIdByToken(RefreshTokenCacheKey cacheKey);
}
