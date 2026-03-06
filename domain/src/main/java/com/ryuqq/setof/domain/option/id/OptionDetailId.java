package com.ryuqq.setof.domain.option.id;

/** 옵션 상세(옵션값) ID Value Object. */
public record OptionDetailId(Long value) {

    public static OptionDetailId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("OptionDetailId 값은 null일 수 없습니다");
        }
        return new OptionDetailId(value);
    }

    public static OptionDetailId forNew() {
        return new OptionDetailId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
