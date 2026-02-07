package com.ryuqq.setof.domain.legacy.review.dto.query;

/**
 * 레거시 작성 가능한 리뷰 검색 조건 DTO.
 *
 * <p>fetchAvailableReviews 전용 검색 조건.
 *
 * @param userId 사용자 ID (필수)
 * @param lastOrderId 커서 페이징용 마지막 주문 ID
 * @param pageSize 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyAvailableReviewSearchCondition(long userId, Long lastOrderId, int pageSize) {

    public static LegacyAvailableReviewSearchCondition of(
            long userId, Long lastOrderId, int pageSize) {
        return new LegacyAvailableReviewSearchCondition(userId, lastOrderId, pageSize);
    }

    public boolean hasLastOrderId() {
        return lastOrderId != null;
    }
}
