package com.ryuqq.setof.domain.discount.vo;

/**
 * 최대 할인 금액 Value Object
 *
 * <p>정률 할인 시 적용되는 최대 할인 금액 상한을 표현합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Long 사용 - 큰 금액 처리 가능
 * </ul>
 *
 * @param value 최대 할인 금액 (원), null이면 상한 없음
 */
public record MaximumDiscountAmount(Long value) {

    private static final long MAX_AMOUNT = 100_000_000L; // 1억원

    /** Compact Constructor - 검증 로직 */
    public MaximumDiscountAmount {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 최대 할인 금액 (원)
     * @return MaximumDiscountAmount 인스턴스
     */
    public static MaximumDiscountAmount of(Long value) {
        return new MaximumDiscountAmount(value);
    }

    /**
     * Static Factory Method - 상한 없음 생성
     *
     * @return 상한 없는 MaximumDiscountAmount 인스턴스
     */
    public static MaximumDiscountAmount unlimited() {
        return new MaximumDiscountAmount(null);
    }

    private static void validate(Long value) {
        if (value == null) {
            return;
        }
        if (value <= 0L) {
            throw new IllegalArgumentException("최대 할인 금액은 0원보다 커야 합니다.");
        }
        if (value > MAX_AMOUNT) {
            throw new IllegalArgumentException("최대 할인 금액은 1억원을 초과할 수 없습니다.");
        }
    }

    /**
     * 계산된 할인 금액에 상한 적용
     *
     * @param discountAmount 계산된 할인 금액
     * @return 상한이 적용된 할인 금액
     */
    public long apply(long discountAmount) {
        if (value == null) {
            return discountAmount;
        }
        return Math.min(discountAmount, value);
    }

    /**
     * 상한이 설정되어 있는지 확인
     *
     * @return 상한이 있으면 true
     */
    public boolean hasLimit() {
        return value != null;
    }

    /**
     * 상한이 없는지 확인
     *
     * @return 상한이 없으면 true
     */
    public boolean isUnlimited() {
        return value == null;
    }
}
