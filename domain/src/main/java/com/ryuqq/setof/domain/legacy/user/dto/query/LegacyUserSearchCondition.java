package com.ryuqq.setof.domain.legacy.user.dto.query;

/**
 * 레거시 사용자 검색 조건 DTO.
 *
 * <p>userId 또는 phoneNumber로 사용자 조회.
 *
 * @param userId 사용자 ID
 * @param phoneNumber 전화번호
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyUserSearchCondition(Long userId, String phoneNumber) {

    public static LegacyUserSearchCondition ofUserId(Long userId) {
        return new LegacyUserSearchCondition(userId, null);
    }

    public static LegacyUserSearchCondition ofPhoneNumber(String phoneNumber) {
        return new LegacyUserSearchCondition(null, phoneNumber);
    }

    public static LegacyUserSearchCondition empty() {
        return new LegacyUserSearchCondition(null, null);
    }

    public boolean hasUserId() {
        return userId != null;
    }

    public boolean hasPhoneNumber() {
        return phoneNumber != null && !phoneNumber.isBlank();
    }
}
