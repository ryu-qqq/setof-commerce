package com.ryuqq.setof.domain.mileage.id;

/** 마일리지 적립 건 ID Value Object. */
public record MileageEntryId(Long value) {

    public static MileageEntryId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("MileageEntryId 값은 null일 수 없습니다");
        }
        return new MileageEntryId(value);
    }

    public static MileageEntryId forNew() {
        return new MileageEntryId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
