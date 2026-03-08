package com.ryuqq.setof.application.review.dto.query;

/**
 * 상품그룹 리뷰 검색 파라미터 DTO.
 *
 * <p>Controller에서 전달받아 Factory에서 SearchCriteria로 변환됩니다.
 *
 * @param productGroupId 상품그룹 ID
 * @param orderType 정렬 타입 (RECOMMEND, HIGH_RATING, RECENT)
 * @param page 페이지 번호 (0-based)
 * @param size 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductGroupReviewSearchParams(
        long productGroupId, String orderType, int page, int size) {

    private static final int DEFAULT_SIZE = 20;

    public int sizeOrDefault() {
        return size > 0 ? size : DEFAULT_SIZE;
    }

    public int pageOrDefault() {
        return Math.max(page, 0);
    }
}
