package com.ryuqq.setof.domain.mileage.id;

/** 마일리지 변동 이력 ID Value Object. */
public record MileageTransactionId(Long value) {

    public static MileageTransactionId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("MileageTransactionId 값은 null일 수 없습니다");
        }
        return new MileageTransactionId(value);
    }

    public static MileageTransactionId forNew() {
        return new MileageTransactionId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
