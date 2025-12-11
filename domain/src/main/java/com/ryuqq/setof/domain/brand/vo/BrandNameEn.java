package com.ryuqq.setof.domain.brand.vo;

import com.ryuqq.setof.domain.brand.exception.InvalidBrandNameEnException;

/**
 * 영문 브랜드명 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>영문 브랜드명 형식 검증 (선택, 최대 255자)
 * </ul>
 *
 * <p>영문 브랜드명은 선택 사항이므로 null 허용, 빈 문자열은 불허.
 *
 * @param value 영문 브랜드명 값 (선택, 최대 255자, null 허용)
 */
public record BrandNameEn(String value) {

    private static final int MAX_LENGTH = 255;

    /** Compact Constructor - 검증 로직 */
    public BrandNameEn {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 영문 브랜드명 문자열 (null 허용)
     * @return BrandNameEn 인스턴스
     * @throws InvalidBrandNameEnException value가 유효하지 않은 경우
     */
    public static BrandNameEn of(String value) {
        return new BrandNameEn(value);
    }

    /**
     * 빈 BrandNameEn 생성 (null 값)
     *
     * @return 값이 null인 BrandNameEn 인스턴스
     */
    public static BrandNameEn empty() {
        return new BrandNameEn(null);
    }

    /**
     * 값 존재 여부 확인
     *
     * @return 값이 존재하면 true
     */
    public boolean hasValue() {
        return value != null && !value.isBlank();
    }

    private static void validate(String value) {
        // null은 허용 (선택 필드)
        if (value == null) {
            return;
        }
        // 빈 문자열은 불허 (null이어야 함)
        if (value.isBlank()) {
            throw new InvalidBrandNameEnException(value);
        }
        if (value.length() > MAX_LENGTH) {
            throw new InvalidBrandNameEnException(value);
        }
    }
}
