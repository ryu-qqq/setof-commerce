package com.ryuqq.setof.domain.member.vo;

import com.ryuqq.setof.domain.common.vo.CacheKey;

/**
 * Refresh Token 캐시 키
 *
 * <p>Redis에서 Refresh Token을 조회하기 위한 캐시 키입니다.
 *
 * <p><strong>키 형식:</strong> {@code cache:refresh-token:{token}}
 *
 * <p><strong>저장 구조:</strong>
 *
 * <ul>
 *   <li>Key: cache:refresh-token:{token}
 *   <li>Value: memberId (UUID 문자열)
 * </ul>
 *
 * @param token Refresh Token 값
 * @author Development Team
 * @since 1.0.0
 */
public record RefreshTokenCacheKey(String token) implements CacheKey {

    private static final String PREFIX = "cache:refresh-token:";

    public RefreshTokenCacheKey {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("token must not be blank");
        }
    }

    @Override
    public String value() {
        return PREFIX + token;
    }

    public static RefreshTokenCacheKey of(String token) {
        return new RefreshTokenCacheKey(token);
    }
}
