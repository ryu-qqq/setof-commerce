package com.ryuqq.setof.domain.category.vo;

/**
 * 카테고리 타입.
 *
 * <p>카테고리의 상품 분류 타입을 표현합니다.
 */
public enum CategoryType {

    /** 없음 */
    NONE,

    /** 의류 */
    CLOTHING,

    /** 신발 (레거시 SHOSE 오타 유지) */
    SHOSE,

    /** 가방 */
    BAG,

    /** 악세서리 */
    ACC
}
