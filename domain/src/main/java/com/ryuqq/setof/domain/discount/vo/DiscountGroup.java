package com.ryuqq.setof.domain.discount.vo;

/**
 * 할인 그룹 Enum
 *
 * <p>할인 정책을 그룹으로 분류합니다. 같은 그룹 내에서는 우선순위가 높은 할인만 적용되고, 다른 그룹 간에는 중복 적용이 가능합니다.
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>PRODUCT: 상품 할인 (상품, 카테고리, 브랜드 대상)
 *   <li>MEMBER: 회원 할인 (등급별, 첫구매 등)
 *   <li>PAYMENT: 결제 할인 (결제수단별)
 * </ul>
 */
public enum DiscountGroup {
    /** 상품 할인 그룹 - 상품, 카테고리, 셀러, 브랜드 대상 */
    PRODUCT("상품 할인"),

    /** 회원 할인 그룹 - 등급별, 첫구매 등 */
    MEMBER("회원 할인"),

    /** 결제 할인 그룹 - 결제수단별 */
    PAYMENT("결제 할인");

    private final String description;

    DiscountGroup(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
