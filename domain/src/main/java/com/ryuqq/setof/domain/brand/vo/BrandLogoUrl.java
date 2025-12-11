package com.ryuqq.setof.domain.brand.vo;

import com.ryuqq.setof.domain.brand.exception.InvalidBrandLogoUrlException;

/**
 * 브랜드 로고 URL Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>URL 형식 검증 (선택, 최대 500자)
 * </ul>
 *
 * <p>로고 URL은 선택 사항이므로 null 허용.
 *
 * @param value 브랜드 로고 URL 값 (선택, 최대 500자, null 허용)
 */
public record BrandLogoUrl(String value) {

    private static final int MAX_LENGTH = 500;
    private static final String URL_PREFIX_HTTP = "http://";
    private static final String URL_PREFIX_HTTPS = "https://";

    /** Compact Constructor - 검증 로직 */
    public BrandLogoUrl {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 브랜드 로고 URL 문자열 (null 허용)
     * @return BrandLogoUrl 인스턴스
     * @throws InvalidBrandLogoUrlException value가 유효하지 않은 경우
     */
    public static BrandLogoUrl of(String value) {
        return new BrandLogoUrl(value);
    }

    /**
     * 빈 BrandLogoUrl 생성 (null 값)
     *
     * @return 값이 null인 BrandLogoUrl 인스턴스
     */
    public static BrandLogoUrl empty() {
        return new BrandLogoUrl(null);
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
            throw new InvalidBrandLogoUrlException(value);
        }
        if (value.length() > MAX_LENGTH) {
            throw new InvalidBrandLogoUrlException(value);
        }
        // URL 형식 검증 (http:// 또는 https://로 시작)
        if (!value.startsWith(URL_PREFIX_HTTP) && !value.startsWith(URL_PREFIX_HTTPS)) {
            throw new InvalidBrandLogoUrlException(value);
        }
    }
}
