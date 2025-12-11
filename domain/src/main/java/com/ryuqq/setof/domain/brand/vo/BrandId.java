package com.ryuqq.setof.domain.brand.vo;

import com.ryuqq.setof.domain.brand.exception.InvalidBrandIdException;

/**
 * 브랜드 식별자 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Long 기반 ID - Auto-increment Primary Key
 * </ul>
 *
 * @param value 브랜드 ID 값
 */
public record BrandId(Long value) {

    /** Compact Constructor - 검증 로직 */
    public BrandId {
        validate(value);
    }

    /**
     * Static Factory Method - 기존 ID로 생성
     *
     * @param value Long 값
     * @return BrandId 인스턴스
     * @throws InvalidBrandIdException value가 null이거나 유효하지 않은 경우
     */
    public static BrandId of(Long value) {
        return new BrandId(value);
    }

    private static void validate(Long value) {
        if (value == null || value <= 0) {
            throw new InvalidBrandIdException(value);
        }
    }
}
