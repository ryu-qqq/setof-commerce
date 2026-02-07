package com.ryuqq.setof.domain.legacy.review.dto.query;

/**
 * 레거시 Review 검색 조건 DTO.
 *
 * <p>fetchProductGroupReviews, fetchMyReviews 공통 검색 조건.
 *
 * @param productGroupId 상품그룹 ID 필터
 * @param userId 사용자 ID 필터 (마이페이지용)
 * @param orderType 정렬 타입 (RECOMMEND, HIGH_RATING, RECENT)
 * @param lastReviewId 커서 페이징용 마지막 리뷰 ID
 * @param pageSize 페이지 크기
 * @param pageNumber 페이지 번호 (offset 페이징용)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyReviewSearchCondition(
        Long productGroupId,
        Long userId,
        String orderType,
        Long lastReviewId,
        int pageSize,
        int pageNumber) {

    public static LegacyReviewSearchCondition ofProductGroup(
            Long productGroupId, String orderType, int pageNumber, int pageSize) {
        return new LegacyReviewSearchCondition(
                productGroupId, null, orderType, null, pageSize, pageNumber);
    }

    public static LegacyReviewSearchCondition ofMyReviews(
            Long userId, Long lastReviewId, int pageSize) {
        return new LegacyReviewSearchCondition(null, userId, null, lastReviewId, pageSize, 0);
    }

    public boolean hasProductGroupId() {
        return productGroupId != null;
    }

    public boolean hasUserId() {
        return userId != null;
    }

    public boolean hasLastReviewId() {
        return lastReviewId != null;
    }

    public boolean isRecommendOrder() {
        return "RECOMMEND".equalsIgnoreCase(orderType);
    }

    public boolean isHighRatingOrder() {
        return "HIGH_RATING".equalsIgnoreCase(orderType);
    }

    public long getOffset() {
        return (long) pageNumber * pageSize;
    }
}
