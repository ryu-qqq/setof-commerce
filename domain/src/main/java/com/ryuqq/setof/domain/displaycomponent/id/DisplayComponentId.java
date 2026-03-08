package com.ryuqq.setof.domain.displaycomponent.id;

/**
 * DisplayComponentId - 전시 컴포넌트 식별자.
 *
 * <p>DOM-ID-001, DOM-ID-002 참조.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DisplayComponentId(Long value) {

    public static DisplayComponentId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("DisplayComponentId must be positive");
        }
        return new DisplayComponentId(value);
    }

    public static DisplayComponentId forNew() {
        return new DisplayComponentId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
