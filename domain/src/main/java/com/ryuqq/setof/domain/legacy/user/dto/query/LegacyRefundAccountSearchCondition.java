package com.ryuqq.setof.domain.legacy.user.dto.query;

/**
 * 레거시 환불 계좌 검색 조건 DTO.
 *
 * @param userId 사용자 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyRefundAccountSearchCondition(Long userId) {

    public static LegacyRefundAccountSearchCondition ofUserId(Long userId) {
        return new LegacyRefundAccountSearchCondition(userId);
    }

    public boolean hasUserId() {
        return userId != null;
    }
}
