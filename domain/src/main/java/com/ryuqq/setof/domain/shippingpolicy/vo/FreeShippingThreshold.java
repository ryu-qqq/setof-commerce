package com.ryuqq.setof.domain.shippingpolicy.vo;

import com.ryuqq.setof.domain.shippingpolicy.exception.InvalidFreeShippingThresholdException;

/**
 * 무료 배송 기준 금액 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>nullable 허용 - null이면 무료 배송 미적용
 *   <li>값이 있으면 양수여야 함
 * </ul>
 *
 * @param value 무료 배송 기준 금액 (원) - null이면 무료 배송 미적용
 */
public record FreeShippingThreshold(Integer value) {

    /** Compact Constructor - 검증 로직 */
    public FreeShippingThreshold {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 무료 배송 기준 금액 (원) - null이면 무료 배송 미적용
     * @return FreeShippingThreshold 인스턴스
     * @throws InvalidFreeShippingThresholdException value가 0 이하인 경우
     */
    public static FreeShippingThreshold of(Integer value) {
        return new FreeShippingThreshold(value);
    }

    /**
     * 값 존재 여부 확인
     *
     * @return 무료 배송 기준이 설정되어 있으면 true
     */
    public boolean hasValue() {
        return value != null;
    }

    /**
     * 무료 배송 해당 여부 확인
     *
     * @param orderAmount 주문 금액
     * @return 주문 금액이 무료 배송 기준 이상이면 true
     */
    public boolean isFreeShipping(int orderAmount) {
        return hasValue() && orderAmount >= value;
    }

    private static void validate(Integer value) {
        if (value == null) {
            return;
        }
        if (value <= 0) {
            throw new InvalidFreeShippingThresholdException(value, "무료 배송 기준 금액은 양수여야 합니다.");
        }
    }
}
