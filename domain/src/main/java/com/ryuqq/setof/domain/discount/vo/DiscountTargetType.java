package com.ryuqq.setof.domain.discount.vo;

/**
 * 할인 적용 대상 타입 Enum
 *
 * <p>할인이 적용되는 대상의 종류를 정의합니다.
 */
public enum DiscountTargetType {
    /** 전체 상품 대상 */
    ALL("전체 상품"),

    /** 특정 상품 대상 */
    PRODUCT("특정 상품"),

    /** 특정 카테고리 대상 */
    CATEGORY("카테고리"),

    /** 특정 셀러 대상 */
    SELLER("셀러"),

    /** 특정 브랜드 대상 */
    BRAND("브랜드");

    private final String description;

    DiscountTargetType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 특정 ID가 필요한 타입인지 확인
     *
     * @return ID 목록이 필요하면 true
     */
    public boolean requiresTargetIds() {
        return this != ALL;
    }
}
