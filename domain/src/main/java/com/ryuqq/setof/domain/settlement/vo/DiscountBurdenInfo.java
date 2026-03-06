package com.ryuqq.setof.domain.settlement.vo;

import com.ryuqq.setof.domain.common.vo.Money;

/** 할인 부담 정보 Value Object. */
public record DiscountBurdenInfo(Money discountAmount, double sellerBurdenRate) {

    public DiscountBurdenInfo {
        if (discountAmount == null) {
            throw new IllegalArgumentException("할인금액은 필수입니다");
        }
        if (sellerBurdenRate < 0 || sellerBurdenRate > 100) {
            throw new IllegalArgumentException("판매자 부담 비율은 0 ~ 100 사이여야 합니다: " + sellerBurdenRate);
        }
    }

    public static DiscountBurdenInfo of(Money discountAmount, double sellerBurdenRate) {
        return new DiscountBurdenInfo(discountAmount, sellerBurdenRate);
    }

    public static DiscountBurdenInfo none() {
        return new DiscountBurdenInfo(Money.zero(), 0);
    }

    /** 판매자 부담 할인 금액 계산. */
    public int sellerBurdenAmount() {
        return (int) (discountAmount.value() * sellerBurdenRate / 100);
    }
}
