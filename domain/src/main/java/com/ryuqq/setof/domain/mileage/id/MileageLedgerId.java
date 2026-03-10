package com.ryuqq.setof.domain.mileage.id;

/** 마일리지 원장 ID Value Object. */
public record MileageLedgerId(Long value) {

    public static MileageLedgerId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("MileageLedgerId 값은 null일 수 없습니다");
        }
        return new MileageLedgerId(value);
    }

    public static MileageLedgerId forNew() {
        return new MileageLedgerId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
