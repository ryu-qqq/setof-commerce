package com.ryuqq.setof.domain.mileage.vo;

/** 마일리지 금액 Value Object. 양수만 허용합니다. */
public record MileageAmount(long value) {

    public MileageAmount {
        if (value < 0) {
            throw new IllegalArgumentException("마일리지 금액은 0 이상이어야 합니다: " + value);
        }
    }

    public static MileageAmount of(long value) {
        return new MileageAmount(value);
    }

    public static MileageAmount zero() {
        return new MileageAmount(0);
    }

    public boolean isZero() {
        return value == 0;
    }

    public MileageAmount add(long amount) {
        return new MileageAmount(this.value + amount);
    }

    public MileageAmount subtract(long amount) {
        if (this.value < amount) {
            throw new IllegalArgumentException(
                    String.format("차감 금액(%d)이 보유 금액(%d)을 초과합니다", amount, this.value));
        }
        return new MileageAmount(this.value - amount);
    }
}
