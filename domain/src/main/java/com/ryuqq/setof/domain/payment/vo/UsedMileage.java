package com.ryuqq.setof.domain.payment.vo;

/** 사용 마일리지 Value Object. */
public record UsedMileage(long amount) {

    public UsedMileage {
        if (amount < 0) {
            throw new IllegalArgumentException("마일리지는 0 이상이어야 합니다: " + amount);
        }
    }

    public static UsedMileage of(long amount) {
        return new UsedMileage(amount);
    }

    public static UsedMileage zero() {
        return new UsedMileage(0);
    }

    public boolean isUsed() {
        return amount > 0;
    }
}
