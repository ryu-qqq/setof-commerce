package com.ryuqq.setof.domain.legacy.cart.dto.query;

/**
 * LegacyCartSearchCondition - 레거시 장바구니 검색 조건 DTO.
 *
 * <p>Repository 검색 파라미터용 DTO입니다.
 *
 * @param userId 사용자 ID
 * @param lastCartId 커서 페이징용 마지막 장바구니 ID
 * @param pageSize 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyCartSearchCondition(Long userId, Long lastCartId, Integer pageSize) {

    /**
     * 사용자 ID만 사용하는 생성자 (카운트 조회용).
     *
     * @param userId 사용자 ID
     * @return LegacyCartSearchCondition
     */
    public static LegacyCartSearchCondition ofUserId(Long userId) {
        return new LegacyCartSearchCondition(userId, null, null);
    }

    /**
     * 커서 페이징용 생성자.
     *
     * @param userId 사용자 ID
     * @param lastCartId 마지막 장바구니 ID
     * @param pageSize 페이지 크기
     * @return LegacyCartSearchCondition
     */
    public static LegacyCartSearchCondition ofCursor(
            Long userId, Long lastCartId, Integer pageSize) {
        return new LegacyCartSearchCondition(userId, lastCartId, pageSize);
    }

    /**
     * 빈 조건 생성자.
     *
     * @return LegacyCartSearchCondition
     */
    public static LegacyCartSearchCondition empty() {
        return new LegacyCartSearchCondition(null, null, null);
    }

    /**
     * 사용자 ID 존재 여부.
     *
     * @return 사용자 ID가 있으면 true
     */
    public boolean hasUserId() {
        return userId != null;
    }

    /**
     * 커서 조건 존재 여부.
     *
     * @return 마지막 장바구니 ID가 있으면 true
     */
    public boolean hasCursor() {
        return lastCartId != null;
    }

    /**
     * 페이지 크기 반환 (기본값 20).
     *
     * @return 페이지 크기
     */
    public int getPageSizeOrDefault() {
        return pageSize != null ? pageSize : 20;
    }
}
