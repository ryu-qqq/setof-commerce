package com.ryuqq.setof.domain.refundpolicy.vo;

import com.ryuqq.setof.domain.refundpolicy.exception.InvalidRefundDeliveryCostException;

/**
 * 반품 배송비 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>0 이상의 정수 (음수 불가)
 * </ul>
 *
 * @param value 반품 배송비 값 (원)
 */
public record RefundDeliveryCost(Integer value) {

    /** Compact Constructor - 검증 로직 */
    public RefundDeliveryCost {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 반품 배송비 값 (원)
     * @return RefundDeliveryCost 인스턴스
     * @throws InvalidRefundDeliveryCostException value가 null이거나 음수인 경우
     */
    public static RefundDeliveryCost of(Integer value) {
        return new RefundDeliveryCost(value);
    }

    /**
     * 무료 반품 여부 확인
     *
     * @return 반품 배송비가 0이면 true
     */
    public boolean isFree() {
        return value == 0;
    }

    private static void validate(Integer value) {
        if (value == null) {
            throw new InvalidRefundDeliveryCostException(null, "반품 배송비는 필수입니다.");
        }
        if (value < 0) {
            throw new InvalidRefundDeliveryCostException(value, "반품 배송비는 0 이상이어야 합니다.");
        }
    }
}
