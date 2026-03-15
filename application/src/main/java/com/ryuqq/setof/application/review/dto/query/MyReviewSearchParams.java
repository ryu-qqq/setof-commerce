package com.ryuqq.setof.application.review.dto.query;

/**
 * 내 리뷰 검색 파라미터 DTO.
 *
 * <p>Controller에서 전달받아 Factory에서 SearchCriteria로 변환됩니다.
 *
 * @param legacyUserId 레거시 사용자 ID (nullable)
 * @param memberId 회원 ID (nullable)
 * @param lastReviewId 마지막 리뷰 ID (커서)
 * @param size 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record MyReviewSearchParams(Long legacyUserId, Long memberId, Long lastReviewId, int size) {

    private static final int DEFAULT_SIZE = 20;

    public int sizeOrDefault() {
        return size > 0 ? size : DEFAULT_SIZE;
    }
}
