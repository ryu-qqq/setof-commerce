package com.ryuqq.setof.domain.option.id;

/** 옵션 그룹 ID Value Object. */
public record OptionGroupId(Long value) {

    public static OptionGroupId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("OptionGroupId 값은 null일 수 없습니다");
        }
        return new OptionGroupId(value);
    }

    public static OptionGroupId forNew() {
        return new OptionGroupId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
