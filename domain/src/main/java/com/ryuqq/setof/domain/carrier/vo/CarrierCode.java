package com.ryuqq.setof.domain.carrier.vo;

import com.ryuqq.setof.domain.carrier.exception.InvalidCarrierCodeException;

/**
 * 택배사 코드 Value Object
 *
 * <p>스마트택배 API에서 사용하는 택배사 식별 코드입니다. 예: "04" (CJ대한통운), "05" (한진택배)
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 * </ul>
 *
 * @param value 택배사 코드 값
 */
public record CarrierCode(String value) {

    private static final int MAX_LENGTH = 10;

    /** Compact Constructor - 검증 로직 */
    public CarrierCode {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 택배사 코드 문자열
     * @return CarrierCode 인스턴스
     * @throws InvalidCarrierCodeException value가 유효하지 않은 경우
     */
    public static CarrierCode of(String value) {
        return new CarrierCode(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidCarrierCodeException(value);
        }
        if (value.length() > MAX_LENGTH) {
            throw new InvalidCarrierCodeException(value);
        }
    }
}
