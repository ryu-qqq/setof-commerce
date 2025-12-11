package com.ryuqq.setof.domain.category.vo;

import com.ryuqq.setof.domain.category.exception.InvalidCategoryDepthException;

/**
 * 카테고리 깊이 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>깊이 값 검증 (0 이상)
 * </ul>
 *
 * <p>카테고리 깊이:
 *
 * <ul>
 *   <li>0: 최상위 (대분류)
 *   <li>1: 중분류
 *   <li>2: 소분류
 *   <li>3+: 세부 분류
 * </ul>
 *
 * @param value 카테고리 깊이 값 (0 이상)
 */
public record CategoryDepth(Integer value) {

    /** Compact Constructor - 검증 로직 */
    public CategoryDepth {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 깊이 값
     * @return CategoryDepth 인스턴스
     * @throws InvalidCategoryDepthException value가 유효하지 않은 경우
     */
    public static CategoryDepth of(Integer value) {
        return new CategoryDepth(value);
    }

    /**
     * 최상위 카테고리 여부 확인
     *
     * @return 최상위 카테고리이면 true
     */
    public boolean isRoot() {
        return value == 0;
    }

    /**
     * 중분류 여부 확인
     *
     * @return 중분류이면 true
     */
    public boolean isMiddle() {
        return value == 1;
    }

    /**
     * 소분류 여부 확인
     *
     * @return 소분류이면 true
     */
    public boolean isSmall() {
        return value == 2;
    }

    private static void validate(Integer value) {
        if (value == null || value < 0) {
            throw new InvalidCategoryDepthException(value);
        }
    }
}
