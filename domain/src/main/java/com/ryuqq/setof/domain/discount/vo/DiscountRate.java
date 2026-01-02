package com.ryuqq.setof.domain.discount.vo;

import com.ryuqq.setof.domain.discount.exception.InvalidDiscountRateException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 할인율 Value Object
 *
 * <p>0% ~ 100% 범위의 할인율을 표현합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>BigDecimal 사용 - 정확한 소수점 계산
 * </ul>
 *
 * @param value 할인율 (0.00 ~ 100.00)
 */
public record DiscountRate(BigDecimal value) {

    private static final BigDecimal MIN_RATE = BigDecimal.ZERO;
    private static final BigDecimal MAX_RATE = new BigDecimal("100.00");

    /** Compact Constructor - 검증 로직 */
    public DiscountRate {
        validate(value);
        value = value.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Static Factory Method - BigDecimal로 생성
     *
     * @param value 할인율 (0.00 ~ 100.00)
     * @return DiscountRate 인스턴스
     */
    public static DiscountRate of(BigDecimal value) {
        return new DiscountRate(value);
    }

    /**
     * Static Factory Method - int로 생성
     *
     * @param value 할인율 (0 ~ 100)
     * @return DiscountRate 인스턴스
     */
    public static DiscountRate of(int value) {
        return new DiscountRate(BigDecimal.valueOf(value));
    }

    private static void validate(BigDecimal value) {
        if (value == null) {
            throw new InvalidDiscountRateException(null, "할인율은 필수입니다.");
        }
        if (value.compareTo(MIN_RATE) < 0) {
            throw new InvalidDiscountRateException(value, "할인율은 0% 이상이어야 합니다.");
        }
        if (value.compareTo(MAX_RATE) > 0) {
            throw new InvalidDiscountRateException(value, "할인율은 100%를 초과할 수 없습니다.");
        }
    }

    /**
     * 주어진 금액에 할인율을 적용하여 할인 금액 계산
     *
     * @param amount 원 금액
     * @return 할인 금액
     */
    public long calculateDiscountAmount(long amount) {
        BigDecimal discountAmount =
                BigDecimal.valueOf(amount)
                        .multiply(value)
                        .divide(new BigDecimal("100"), 0, RoundingMode.DOWN);
        return discountAmount.longValue();
    }

    /**
     * 0% 할인인지 확인
     *
     * @return 0%이면 true
     */
    public boolean isZero() {
        return value.compareTo(BigDecimal.ZERO) == 0;
    }
}
