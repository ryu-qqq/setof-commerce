package com.ryuqq.setof.domain.brand.vo;

import com.ryuqq.setof.domain.brand.exception.InvalidBrandCodeException;

/**
 * 브랜드 코드 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>브랜드 코드 형식 검증 (최대 100자)
 * </ul>
 *
 * <p>브랜드 코드는 MarketPlace와 동일한 값을 사용합니다.
 *
 * @param value 브랜드 코드 값 (최대 100자)
 */
public record BrandCode(String value) {

    private static final int MAX_LENGTH = 100;

    /** Compact Constructor - 검증 로직 */
    public BrandCode {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 브랜드 코드 문자열
     * @return BrandCode 인스턴스
     * @throws InvalidBrandCodeException value가 유효하지 않은 경우
     */
    public static BrandCode of(String value) {
        return new BrandCode(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidBrandCodeException(value);
        }
        if (value.length() > MAX_LENGTH) {
            throw new InvalidBrandCodeException(value);
        }
    }
}
