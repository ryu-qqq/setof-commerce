package com.ryuqq.setof.domain.product.vo;

import com.ryuqq.setof.domain.product.exception.InvalidProductGroupNameException;

/**
 * 상품그룹명 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>길이 제한 - 최대 200자
 * </ul>
 *
 * @param value 상품그룹명 값
 */
public record ProductGroupName(String value) {

    private static final int MAX_LENGTH = 200;

    /** Compact Constructor - 검증 로직 */
    public ProductGroupName {
        validate(value);
    }

    /**
     * Static Factory Method - 상품그룹명 생성
     *
     * @param value 상품그룹명 문자열
     * @return ProductGroupName 인스턴스
     * @throws InvalidProductGroupNameException value가 null이거나 빈 문자열이거나 최대 길이 초과인 경우
     */
    public static ProductGroupName of(String value) {
        return new ProductGroupName(value);
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
        if (value == null || value.isBlank()) {
            throw new InvalidProductGroupNameException(value, "상품그룹명은 필수입니다");
        }
        if (value.length() > MAX_LENGTH) {
            throw new InvalidProductGroupNameException(
                    value, String.format("상품그룹명은 %d자를 초과할 수 없습니다", MAX_LENGTH));
        }
    }
}
