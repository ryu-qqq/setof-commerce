package com.ryuqq.setof.domain.member.vo;

/**
 * 배송지 별칭 Value Object.
 *
 * <p>예: "집", "회사", "부모님댁"
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ShippingAddressName(String value) {

    private static final int MAX_LENGTH = 30;

    public ShippingAddressName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("배송지 이름은 필수입니다");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("배송지 이름은 %d자를 초과할 수 없습니다: %d자", MAX_LENGTH, value.length()));
        }
    }

    public static ShippingAddressName of(String value) {
        return new ShippingAddressName(value);
    }
}
