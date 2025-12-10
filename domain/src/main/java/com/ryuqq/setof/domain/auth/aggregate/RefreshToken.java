package com.ryuqq.setof.domain.auth.aggregate;

import com.ryuqq.setof.domain.auth.vo.RefreshTokenCacheKey;
import com.ryuqq.setof.domain.auth.vo.TokenExpiration;
import com.ryuqq.setof.domain.auth.vo.TokenValue;
import java.time.Instant;

/**
 * RefreshToken Aggregate Root
 *
 * <p>Refresh Token의 도메인 모델로서 토큰 생성, 검증, 만료 관련 비즈니스 로직을 캡슐화합니다.
 *
 * <p><strong>Aggregate 책임:</strong>
 *
 * <ul>
 *   <li>토큰 생성 및 유효성 검증
 *   <li>만료 시간 관리
 *   <li>캐시 키 생성
 * </ul>
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>memberId: String UUID로 관리 (Member Aggregate 참조)
 *   <li>직접적인 객체 참조 없음
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public class RefreshToken {

    private final String memberId;
    private final TokenValue tokenValue;
    private final TokenExpiration expiration;
    private final Instant createdAt;

    private RefreshToken(
            String memberId, TokenValue tokenValue, TokenExpiration expiration, Instant createdAt) {
        this.memberId = memberId;
        this.tokenValue = tokenValue;
        this.expiration = expiration;
        this.createdAt = createdAt;
    }

    /**
     * 새 Refresh Token 생성 (권장 팩토리 메서드)
     *
     * <p>신규 RefreshToken 생성 시 사용합니다. Factory를 통해 생성하는 것을 권장합니다.
     *
     * <p>네이밍 컨벤션:
     *
     * <ul>
     *   <li>forNew(): 새 엔티티 생성 (ID 없음)
     *   <li>of(): ID가 있는 엔티티 생성
     *   <li>reconstitute(): 영속성에서 복원
     * </ul>
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @param tokenValue 토큰 값
     * @param expiresInSeconds 만료까지 남은 초
     * @param now 현재 시간
     * @return RefreshToken 인스턴스
     */
    public static RefreshToken forNew(
            String memberId, String tokenValue, long expiresInSeconds, Instant now) {
        validateMemberId(memberId);

        return new RefreshToken(
                memberId,
                TokenValue.of(tokenValue),
                TokenExpiration.of(now, expiresInSeconds),
                now);
    }

    /**
     * 새 Refresh Token 생성 (하위 호환용)
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @param tokenValue 토큰 값
     * @param expiresInSeconds 만료까지 남은 초
     * @param now 현재 시간
     * @return RefreshToken 인스턴스
     * @deprecated forNew() 사용을 권장합니다
     */
    @Deprecated(since = "1.0.0", forRemoval = false)
    public static RefreshToken create(
            String memberId, String tokenValue, long expiresInSeconds, Instant now) {
        return forNew(memberId, tokenValue, expiresInSeconds, now);
    }

    /**
     * 기존 Refresh Token 재구성 (조회용)
     *
     * @param memberId 회원 ID
     * @param tokenValue 토큰 값
     * @param expiresAt 만료 일시
     * @param createdAt 생성 일시
     * @return RefreshToken 인스턴스
     */
    public static RefreshToken reconstitute(
            String memberId, String tokenValue, Instant expiresAt, Instant createdAt) {
        long expiresInSeconds = expiresAt.getEpochSecond() - createdAt.getEpochSecond();

        return new RefreshToken(
                memberId,
                TokenValue.of(tokenValue),
                new TokenExpiration(expiresAt, expiresInSeconds),
                createdAt);
    }

    private static void validateMemberId(String memberId) {
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("memberId must not be blank");
        }
    }

    /**
     * 토큰 만료 여부 확인
     *
     * @param now 현재 시간
     * @return 만료 여부
     */
    public boolean isExpired(Instant now) {
        return expiration.isExpired(now);
    }

    /**
     * 캐시 키 생성
     *
     * @return RefreshTokenCacheKey
     */
    public RefreshTokenCacheKey toCacheKey() {
        return RefreshTokenCacheKey.of(tokenValue.value());
    }

    // ===== Getters (Law of Demeter 준수: 직접 값 반환) =====

    public String getMemberId() {
        return memberId;
    }

    public String getTokenValue() {
        return tokenValue.value();
    }

    public Instant getExpiresAt() {
        return expiration.expiresAt();
    }

    public long getExpiresInSeconds() {
        return expiration.expiresInSeconds();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
