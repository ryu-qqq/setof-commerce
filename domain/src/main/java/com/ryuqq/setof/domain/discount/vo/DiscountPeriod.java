package com.ryuqq.setof.domain.discount.vo;

import java.time.Instant;

/**
 * 할인 유효 기간 Value Object.
 *
 * <p>시작 시점과 종료 시점을 표현합니다. start < end 불변식을 보장합니다.
 *
 * @param startAt 시작 시점 (필수)
 * @param endAt 종료 시점 (필수)
 */
public record DiscountPeriod(Instant startAt, Instant endAt) {

    public DiscountPeriod {
        if (startAt == null) {
            throw new IllegalArgumentException("할인 시작 시점은 필수입니다");
        }
        if (endAt == null) {
            throw new IllegalArgumentException("할인 종료 시점은 필수입니다");
        }
        if (!startAt.isBefore(endAt)) {
            throw new IllegalArgumentException("할인 시작 시점은 종료 시점보다 이전이어야 합니다");
        }
    }

    public static DiscountPeriod of(Instant startAt, Instant endAt) {
        return new DiscountPeriod(startAt, endAt);
    }

    /** 주어진 시점이 유효 기간 내인지 확인 */
    public boolean isActiveAt(Instant now) {
        return !now.isBefore(startAt) && now.isBefore(endAt);
    }

    /** 유효 기간이 아직 시작되지 않았는지 확인 */
    public boolean isNotStarted(Instant now) {
        return now.isBefore(startAt);
    }

    /** 유효 기간이 만료되었는지 확인 */
    public boolean isExpired(Instant now) {
        return !now.isBefore(endAt);
    }
}
