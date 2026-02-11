package com.ryuqq.setof.domain.legacy.user.dto.query;

/**
 * 레거시 마이페이지 검색 조건 DTO.
 *
 * @param userId 사용자 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyMyPageSearchCondition(Long userId) {

    public static LegacyMyPageSearchCondition ofUserId(Long userId) {
        return new LegacyMyPageSearchCondition(userId);
    }

    public boolean hasUserId() {
        return userId != null;
    }
}
