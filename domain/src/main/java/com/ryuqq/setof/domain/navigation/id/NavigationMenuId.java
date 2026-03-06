package com.ryuqq.setof.domain.navigation.id;

/**
 * NavigationMenu 식별자 Value Object.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record NavigationMenuId(Long value) {

    public static NavigationMenuId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("NavigationMenuId 값은 null일 수 없습니다");
        }
        return new NavigationMenuId(value);
    }

    public static NavigationMenuId forNew() {
        return new NavigationMenuId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
