package com.ryuqq.setof.application.auth.port.out.cache;

import com.ryuqq.setof.domain.auth.vo.RefreshTokenCacheKey;

/**
 * Refresh Token Cache Command Port
 *
 * <p>Refresh Token 캐시(Redis) 저장/삭제를 담당하는 Port-Out 인터페이스
 *
 * <p><strong>저장 구조:</strong>
 *
 * <ul>
 *   <li>Key: cache:refresh-token:{token}
 *   <li>Value: memberId (UUID 문자열)
 * </ul>
 *
 * <p><strong>구현체:</strong>
 *
 * <ul>
 *   <li>adapter-out-redis: RefreshTokenCacheAdapter
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RefreshTokenCacheCommandPort {

    /**
     * Refresh Token 캐시 저장
     *
     * @param cacheKey 캐시 키 (token 기반)
     * @param memberId 회원 ID (UUID 문자열)
     * @param expiresInSeconds 만료 시간 (초)
     */
    void persist(RefreshTokenCacheKey cacheKey, String memberId, long expiresInSeconds);

    /**
     * Refresh Token 삭제
     *
     * @param cacheKey 캐시 키 (token 기반)
     */
    void delete(RefreshTokenCacheKey cacheKey);
}
