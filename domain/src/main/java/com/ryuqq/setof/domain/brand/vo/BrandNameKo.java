package com.ryuqq.setof.domain.brand.vo;

import com.ryuqq.setof.domain.brand.exception.InvalidBrandNameKoException;

/**
 * 한글 브랜드명 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>한글 브랜드명 형식 검증 (필수, 최대 255자)
 * </ul>
 *
 * @param value 한글 브랜드명 값 (필수, 최대 255자)
 */
public record BrandNameKo(String value) {

    private static final int MAX_LENGTH = 255;

    /** Compact Constructor - 검증 로직 */
    public BrandNameKo {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 한글 브랜드명 문자열
     * @return BrandNameKo 인스턴스
     * @throws InvalidBrandNameKoException value가 유효하지 않은 경우
     */
    public static BrandNameKo of(String value) {
        return new BrandNameKo(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidBrandNameKoException(value);
        }
        if (value.length() > MAX_LENGTH) {
            throw new InvalidBrandNameKoException(value);
        }
    }
}
