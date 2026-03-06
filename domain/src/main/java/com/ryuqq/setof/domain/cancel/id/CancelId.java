package com.ryuqq.setof.domain.cancel.id;

/** 취소 ID Value Object. */
public record CancelId(Long value) {

    public static CancelId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("CancelId 값은 null일 수 없습니다");
        }
        return new CancelId(value);
    }

    public static CancelId forNew() {
        return new CancelId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
