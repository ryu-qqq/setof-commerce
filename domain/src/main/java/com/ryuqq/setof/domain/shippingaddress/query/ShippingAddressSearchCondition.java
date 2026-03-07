package com.ryuqq.setof.domain.shippingaddress.query;

/**
 * 배송지 검색 조건 DTO.
 *
 * @param userId 사용자 ID
 * @param shippingAddressId 배송지 ID (단건 조회 시)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ShippingAddressSearchCondition(Long userId, Long shippingAddressId) {

    public static ShippingAddressSearchCondition ofUserId(Long userId) {
        return new ShippingAddressSearchCondition(userId, null);
    }

    public static ShippingAddressSearchCondition of(Long userId, Long shippingAddressId) {
        return new ShippingAddressSearchCondition(userId, shippingAddressId);
    }

    public boolean hasUserId() {
        return userId != null;
    }

    public boolean hasShippingAddressId() {
        return shippingAddressId != null;
    }
}
