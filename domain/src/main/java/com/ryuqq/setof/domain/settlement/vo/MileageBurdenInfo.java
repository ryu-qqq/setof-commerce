package com.ryuqq.setof.domain.settlement.vo;

/** 마일리지 부담 정보 Value Object. */
public record MileageBurdenInfo(long mileageAmount, double sellerBurdenRate) {

    public MileageBurdenInfo {
        if (mileageAmount < 0) {
            throw new IllegalArgumentException("마일리지는 0 이상이어야 합니다: " + mileageAmount);
        }
        if (sellerBurdenRate < 0 || sellerBurdenRate > 100) {
            throw new IllegalArgumentException("판매자 부담 비율은 0 ~ 100 사이여야 합니다: " + sellerBurdenRate);
        }
    }

    public static MileageBurdenInfo of(long mileageAmount, double sellerBurdenRate) {
        return new MileageBurdenInfo(mileageAmount, sellerBurdenRate);
    }

    public static MileageBurdenInfo none() {
        return new MileageBurdenInfo(0, 0);
    }

    /** 판매자 부담 마일리지 금액 계산. */
    public long sellerBurdenAmount() {
        return (long) (mileageAmount * sellerBurdenRate / 100);
    }
}
