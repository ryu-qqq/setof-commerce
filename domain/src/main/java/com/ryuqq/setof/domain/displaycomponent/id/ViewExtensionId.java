package com.ryuqq.setof.domain.displaycomponent.id;

/**
 * ViewExtensionId - 뷰 확장 식별자.
 *
 * <p>DOM-ID-001, DOM-ID-002 참조.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ViewExtensionId(Long value) {

    public static ViewExtensionId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("ViewExtensionId must be positive");
        }
        return new ViewExtensionId(value);
    }

    public static ViewExtensionId forNew() {
        return new ViewExtensionId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
