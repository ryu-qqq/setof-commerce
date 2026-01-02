package com.ryuqq.setof.domain.carrier.vo;

import com.ryuqq.setof.domain.carrier.exception.InvalidCarrierNameException;

/**
 * 택배사명 Value Object
 *
 * <p>택배사의 표시 이름입니다. 예: "CJ대한통운", "한진택배"
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 * </ul>
 *
 * @param value 택배사명 값
 */
public record CarrierName(String value) {

    private static final int MAX_LENGTH = 50;

    /** Compact Constructor - 검증 로직 */
    public CarrierName {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 택배사명 문자열
     * @return CarrierName 인스턴스
     * @throws InvalidCarrierNameException value가 유효하지 않은 경우
     */
    public static CarrierName of(String value) {
        return new CarrierName(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidCarrierNameException(value);
        }
        if (value.length() > MAX_LENGTH) {
            throw new InvalidCarrierNameException(value);
        }
    }
}
