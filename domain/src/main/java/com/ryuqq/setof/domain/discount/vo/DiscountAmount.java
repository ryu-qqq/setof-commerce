package com.ryuqq.setof.domain.discount.vo;

import com.ryuqq.setof.domain.discount.exception.InvalidDiscountAmountException;

/**
 * 할인 금액 Value Object
 *
 * <p>정액 할인 시 적용되는 금액을 표현합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Long 사용 - 큰 금액 처리 가능
 * </ul>
 *
 * @param value 할인 금액 (원)
 */
public record DiscountAmount(Long value) {

    private static final long MIN_AMOUNT = 0L;
    private static final long MAX_AMOUNT = 100_000_000L; // 1억원

    /** Compact Constructor - 검증 로직 */
    public DiscountAmount {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 할인 금액 (원)
     * @return DiscountAmount 인스턴스
     */
    public static DiscountAmount of(Long value) {
        return new DiscountAmount(value);
    }

    /**
     * Static Factory Method - 0원 생성
     *
     * @return 0원 DiscountAmount 인스턴스
     */
    public static DiscountAmount zero() {
        return new DiscountAmount(0L);
    }

    private static void validate(Long value) {
        if (value == null) {
            throw new InvalidDiscountAmountException(null, "할인 금액은 필수입니다.");
        }
        if (value < MIN_AMOUNT) {
            throw new InvalidDiscountAmountException(value, "할인 금액은 0원 이상이어야 합니다.");
        }
        if (value > MAX_AMOUNT) {
            throw new InvalidDiscountAmountException(value, "할인 금액은 1억원을 초과할 수 없습니다.");
        }
    }

    /**
     * 주어진 금액에서 할인 금액 적용 (최대 원 금액까지만 할인)
     *
     * @param originalAmount 원 금액
     * @return 실제 적용되는 할인 금액
     */
    public long applyTo(long originalAmount) {
        return Math.min(value, originalAmount);
    }

    /**
     * 0원 할인인지 확인
     *
     * @return 0원이면 true
     */
    public boolean isZero() {
        return value == 0L;
    }
}
