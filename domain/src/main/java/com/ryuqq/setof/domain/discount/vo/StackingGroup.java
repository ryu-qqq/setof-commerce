package com.ryuqq.setof.domain.discount.vo;

/**
 * 할인 스태킹 그룹.
 *
 * <p>그룹 내에서는 우선순위 최상위 1개만 적용(배타적)하고, 그룹 간에는 applicationOrder 순서대로 순차 적용(복합)합니다.
 *
 * <p>가격 적용 흐름:
 *
 * <pre>
 * regularPrice
 *   ↓ SELLER_INSTANT (셀러 즉시할인)
 *   ↓ PLATFORM_INSTANT (플랫폼 즉시할인)
 * salePrice
 *   ↓ COUPON (쿠폰 할인)
 * finalPrice
 * </pre>
 */
public enum StackingGroup {

    /** 셀러 즉시할인 (1순위) */
    SELLER_INSTANT(1),

    /** 플랫폼 즉시할인 (2순위) */
    PLATFORM_INSTANT(2),

    /** 쿠폰 할인 (3순위) */
    COUPON(3);

    private final int applicationOrder;

    StackingGroup(int applicationOrder) {
        this.applicationOrder = applicationOrder;
    }

    /** 적용 순서 (낮을수록 먼저 적용) */
    public int applicationOrder() {
        return applicationOrder;
    }
}
