package com.ryuqq.setof.domain.legacy.user.dto.query;

/**
 * 레거시 배송지 검색 조건 DTO.
 *
 * @param userId 사용자 ID
 * @param shippingAddressId 배송지 ID (단건 조회 시)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyShippingAddressSearchCondition(Long userId, Long shippingAddressId) {

    public static LegacyShippingAddressSearchCondition ofUserId(Long userId) {
        return new LegacyShippingAddressSearchCondition(userId, null);
    }

    public static LegacyShippingAddressSearchCondition of(Long userId, Long shippingAddressId) {
        return new LegacyShippingAddressSearchCondition(userId, shippingAddressId);
    }

    public boolean hasUserId() {
        return userId != null;
    }

    public boolean hasShippingAddressId() {
        return shippingAddressId != null;
    }
}
