package com.ryuqq.setof.domain.shippingaddress.vo;

import com.ryuqq.setof.domain.shippingaddress.exception.InvalidAddressNameException;

/**
 * 배송지 이름 Value Object
 *
 * <p>사용자가 배송지를 구분하기 위해 지정하는 이름입니다. 예: "집", "회사", "부모님댁"
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>배송지 이름 길이 검증 (1~30자)
 * </ul>
 *
 * @param value 배송지 이름 값
 */
public record AddressName(String value) {

    private static final int MAX_LENGTH = 30;

    /** Compact Constructor - 검증 로직 */
    public AddressName {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 배송지 이름 문자열
     * @return AddressName 인스턴스
     * @throws InvalidAddressNameException value가 유효하지 않은 경우
     */
    public static AddressName of(String value) {
        return new AddressName(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidAddressNameException(value);
        }
        if (value.length() > MAX_LENGTH) {
            throw new InvalidAddressNameException(value);
        }
    }
}
