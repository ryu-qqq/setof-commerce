package com.ryuqq.setof.domain.shippingpolicy.vo;

import com.ryuqq.setof.domain.shippingpolicy.exception.InvalidShippingPolicyIdException;

/**
 * 배송 정책 식별자 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Long 기반 ID - Auto-increment Primary Key
 * </ul>
 *
 * @param value 배송 정책 ID 값
 */
public record ShippingPolicyId(Long value) {

    /** Compact Constructor - 검증 로직 */
    public ShippingPolicyId {
        validate(value);
    }

    /**
     * Static Factory Method - 기존 ID로 생성
     *
     * @param value Long 값
     * @return ShippingPolicyId 인스턴스
     * @throws InvalidShippingPolicyIdException value가 null이거나 유효하지 않은 경우
     */
    public static ShippingPolicyId of(Long value) {
        return new ShippingPolicyId(value);
    }

    private static void validate(Long value) {
        if (value == null || value <= 0) {
            throw new InvalidShippingPolicyIdException(value);
        }
    }
}
