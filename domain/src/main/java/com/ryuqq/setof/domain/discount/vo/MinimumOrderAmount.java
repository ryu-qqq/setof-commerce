package com.ryuqq.setof.domain.discount.vo;

import com.ryuqq.setof.domain.discount.exception.InvalidMinimumOrderAmountException;

/**
 * 최소 주문 금액 Value Object
 *
 * <p>할인 정책이 적용되기 위한 최소 주문 금액을 표현합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Long 사용 - 큰 금액 처리 가능
 * </ul>
 *
 * @param value 최소 주문 금액 (원)
 */
public record MinimumOrderAmount(Long value) {

    private static final long MAX_AMOUNT = 100_000_000L; // 1억원

    /** Compact Constructor - 검증 로직 */
    public MinimumOrderAmount {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 최소 주문 금액 (원)
     * @return MinimumOrderAmount 인스턴스
     */
    public static MinimumOrderAmount of(Long value) {
        return new MinimumOrderAmount(value);
    }

    /**
     * Static Factory Method - 제한 없음 생성
     *
     * @return 최소 금액 0원 MinimumOrderAmount 인스턴스
     */
    public static MinimumOrderAmount noMinimum() {
        return new MinimumOrderAmount(0L);
    }

    private static void validate(Long value) {
        if (value == null) {
            throw new InvalidMinimumOrderAmountException(null, "최소 주문 금액은 필수입니다.");
        }
        if (value < 0L) {
            throw new InvalidMinimumOrderAmountException(value, "최소 주문 금액은 0원 이상이어야 합니다.");
        }
        if (value > MAX_AMOUNT) {
            throw new InvalidMinimumOrderAmountException(value, "최소 주문 금액은 1억원을 초과할 수 없습니다.");
        }
    }

    /**
     * 주문 금액이 최소 금액 이상인지 확인
     *
     * @param orderAmount 주문 금액
     * @return 최소 금액 이상이면 true
     */
    public boolean isSatisfiedBy(long orderAmount) {
        return orderAmount >= value;
    }

    /**
     * 최소 금액 제한이 없는지 확인
     *
     * @return 최소 금액이 0원이면 true
     */
    public boolean hasNoMinimum() {
        return value == 0L;
    }
}
