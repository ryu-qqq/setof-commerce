package com.ryuqq.setof.domain.settlement.vo;

/** 정산 수수료율 Value Object. 0 ~ 100 사이의 퍼센트 값. */
public record CommissionRate(double value) {

    public CommissionRate {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("수수료율은 0 ~ 100 사이여야 합니다: " + value);
        }
    }

    public static CommissionRate of(double value) {
        return new CommissionRate(value);
    }

    public static CommissionRate zero() {
        return new CommissionRate(0);
    }
}
