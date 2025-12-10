package com.ryuqq.setof.domain.shippingaddress.vo;

import com.ryuqq.setof.domain.shippingaddress.exception.InvalidShippingAddressIdException;

/**
 * 배송지 식별자 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Long 기반 ID - Auto-increment Primary Key
 * </ul>
 *
 * @param value 배송지 ID 값
 */
public record ShippingAddressId(Long value) {

    /** Compact Constructor - 검증 로직 */
    public ShippingAddressId {
        validate(value);
    }

    /**
     * Static Factory Method - 기존 ID로 생성
     *
     * @param value Long 값
     * @return ShippingAddressId 인스턴스
     * @throws InvalidShippingAddressIdException value가 null이거나 유효하지 않은 경우
     */
    public static ShippingAddressId of(Long value) {
        return new ShippingAddressId(value);
    }

    private static void validate(Long value) {
        if (value == null || value <= 0) {
            throw new InvalidShippingAddressIdException(value);
        }
    }
}
