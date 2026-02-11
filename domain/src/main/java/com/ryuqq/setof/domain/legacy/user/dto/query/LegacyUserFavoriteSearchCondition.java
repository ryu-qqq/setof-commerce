package com.ryuqq.setof.domain.legacy.user.dto.query;

/**
 * 레거시 찜 목록 검색 조건 DTO.
 *
 * <p>커서 기반 페이징 지원.
 *
 * @param userId 사용자 ID
 * @param lastFavoriteId 커서 기반 페이징용 마지막 찜 ID
 * @param pageSize 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyUserFavoriteSearchCondition(Long userId, Long lastFavoriteId, int pageSize) {

    public static LegacyUserFavoriteSearchCondition of(
            Long userId, Long lastFavoriteId, int pageSize) {
        return new LegacyUserFavoriteSearchCondition(userId, lastFavoriteId, pageSize);
    }

    public static LegacyUserFavoriteSearchCondition ofUserId(Long userId, int pageSize) {
        return new LegacyUserFavoriteSearchCondition(userId, null, pageSize);
    }

    public boolean hasUserId() {
        return userId != null;
    }

    public boolean hasLastFavoriteId() {
        return lastFavoriteId != null;
    }
}
