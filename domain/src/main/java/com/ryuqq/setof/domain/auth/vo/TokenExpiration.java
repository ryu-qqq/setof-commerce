package com.ryuqq.setof.domain.auth.vo;

import java.time.Instant;

/**
 * Token Expiration - 토큰 만료 정보 VO
 *
 * <p>토큰의 만료 시간과 만료 여부를 관리합니다.
 *
 * <p><strong>불변 객체:</strong>
 *
 * <ul>
 *   <li>생성 후 값 변경 불가
 *   <li>만료 여부는 조회 시점 기준으로 계산
 * </ul>
 *
 * @param expiresAt 만료 일시 (UTC 기준)
 * @param expiresInSeconds 만료까지 남은 초 (생성 시점 기준)
 * @author development-team
 * @since 1.0.0
 */
public record TokenExpiration(Instant expiresAt, long expiresInSeconds) {

    public TokenExpiration {
        if (expiresAt == null) {
            throw new IllegalArgumentException("expiresAt must not be null");
        }
        if (expiresInSeconds <= 0) {
            throw new IllegalArgumentException("expiresInSeconds must be positive");
        }
    }

    /**
     * 현재 시간 기준 만료 정보 생성
     *
     * @param now 현재 시간
     * @param expiresInSeconds 만료까지 남은 초
     * @return TokenExpiration 인스턴스
     */
    public static TokenExpiration of(Instant now, long expiresInSeconds) {
        Instant expiresAt = now.plusSeconds(expiresInSeconds);
        return new TokenExpiration(expiresAt, expiresInSeconds);
    }

    /**
     * 토큰이 만료되었는지 확인
     *
     * @param now 현재 시간
     * @return 만료 여부
     */
    public boolean isExpired(Instant now) {
        return now.isAfter(expiresAt);
    }
}
