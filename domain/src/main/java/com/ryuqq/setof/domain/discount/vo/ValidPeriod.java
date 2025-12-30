package com.ryuqq.setof.domain.discount.vo;

import com.ryuqq.setof.domain.discount.exception.InvalidValidPeriodException;
import java.time.Instant;

/**
 * 유효 기간 Value Object
 *
 * <p>할인 정책의 적용 가능 기간을 표현합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Instant 사용 - 타임존 독립적 시간 처리
 * </ul>
 *
 * @param startAt 시작 일시
 * @param endAt 종료 일시
 */
public record ValidPeriod(Instant startAt, Instant endAt) {

    /** Compact Constructor - 검증 로직 */
    public ValidPeriod {
        validate(startAt, endAt);
    }

    /**
     * Static Factory Method
     *
     * @param startAt 시작 일시
     * @param endAt 종료 일시
     * @return ValidPeriod 인스턴스
     */
    public static ValidPeriod of(Instant startAt, Instant endAt) {
        return new ValidPeriod(startAt, endAt);
    }

    /**
     * Static Factory Method - 무제한 기간 생성
     *
     * <p>시작: 현재, 종료: 9999-12-31
     *
     * @return 무제한 ValidPeriod 인스턴스
     */
    public static ValidPeriod unlimited() {
        return new ValidPeriod(Instant.now(), Instant.parse("9999-12-31T23:59:59Z"));
    }

    private static void validate(Instant startAt, Instant endAt) {
        if (startAt == null) {
            throw new InvalidValidPeriodException(null, endAt, "시작 일시는 필수입니다.");
        }
        if (endAt == null) {
            throw new InvalidValidPeriodException(startAt, null, "종료 일시는 필수입니다.");
        }
        if (startAt.isAfter(endAt)) {
            throw new InvalidValidPeriodException(startAt, endAt, "시작 일시는 종료 일시보다 이전이어야 합니다.");
        }
    }

    /**
     * 특정 시점이 유효 기간 내인지 확인
     *
     * @param instant 확인할 시점
     * @return 유효 기간 내이면 true
     */
    public boolean isWithinPeriod(Instant instant) {
        return !instant.isBefore(startAt) && !instant.isAfter(endAt);
    }

    /**
     * 현재 시점이 유효 기간 내인지 확인
     *
     * @return 현재 유효하면 true
     */
    public boolean isCurrentlyValid() {
        return isWithinPeriod(Instant.now());
    }

    /**
     * 유효 기간이 만료되었는지 확인
     *
     * @return 만료되었으면 true
     */
    public boolean isExpired() {
        return Instant.now().isAfter(endAt);
    }

    /**
     * 유효 기간이 아직 시작되지 않았는지 확인
     *
     * @return 시작 전이면 true
     */
    public boolean isNotStarted() {
        return Instant.now().isBefore(startAt);
    }

    /**
     * 다른 유효 기간과 겹치는지 확인
     *
     * @param other 비교할 유효 기간
     * @return 겹치면 true
     */
    public boolean overlaps(ValidPeriod other) {
        return !this.endAt.isBefore(other.startAt) && !other.endAt.isBefore(this.startAt);
    }
}
