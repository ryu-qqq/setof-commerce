package com.ryuqq.setof.domain.member.id;

/**
 * 배송지 ID Value Object.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ShippingAddressId(Long value) {

    public static ShippingAddressId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("ShippingAddressId 값은 null일 수 없습니다");
        }
        return new ShippingAddressId(value);
    }

    public static ShippingAddressId forNew() {
        return new ShippingAddressId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
