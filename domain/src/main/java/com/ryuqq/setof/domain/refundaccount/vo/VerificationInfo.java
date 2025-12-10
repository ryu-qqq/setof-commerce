package com.ryuqq.setof.domain.refundaccount.vo;

import java.time.Clock;
import java.time.Instant;

/**
 * 계좌 검증 정보 Value Object (Composite VO)
 *
 * <p>계좌 검증 상태와 검증 일시를 함께 관리합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 * </ul>
 *
 * @param verified 검증 완료 여부
 * @param verifiedAt 검증 완료 일시 (nullable)
 */
public record VerificationInfo(boolean verified, Instant verifiedAt) {

    /**
     * Static Factory Method - 미검증 상태 생성
     *
     * @return 미검증 상태의 VerificationInfo 인스턴스
     */
    public static VerificationInfo unverified() {
        return new VerificationInfo(false, null);
    }

    /**
     * Static Factory Method - 검증 완료 상태 생성
     *
     * @param clock 시간 제공자
     * @return 검증 완료 상태의 VerificationInfo 인스턴스
     */
    public static VerificationInfo verified(Clock clock) {
        return new VerificationInfo(true, clock.instant());
    }

    /**
     * Static Factory Method - 검증 완료 상태 생성 (특정 시각)
     *
     * @param verifiedAt 검증 완료 일시
     * @return 검증 완료 상태의 VerificationInfo 인스턴스
     */
    public static VerificationInfo verifiedAt(Instant verifiedAt) {
        return new VerificationInfo(true, verifiedAt);
    }

    /**
     * Static Factory Method - Persistence 복원용
     *
     * @param verified 검증 상태
     * @param verifiedAt 검증 일시
     * @return VerificationInfo 인스턴스
     */
    public static VerificationInfo reconstitute(boolean verified, Instant verifiedAt) {
        return new VerificationInfo(verified, verifiedAt);
    }

    /**
     * 검증 완료 여부 확인
     *
     * @return 검증 완료 상태이면 true
     */
    public boolean isVerified() {
        return verified;
    }

    /**
     * 미검증 상태 여부 확인
     *
     * @return 미검증 상태이면 true
     */
    public boolean isUnverified() {
        return !verified;
    }
}
