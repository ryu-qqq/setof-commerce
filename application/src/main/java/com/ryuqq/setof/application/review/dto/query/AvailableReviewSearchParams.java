package com.ryuqq.setof.application.review.dto.query;

/**
 * 작성 가능한 리뷰 검색 파라미터 (Application Layer DTO).
 *
 * <p>Controller에서 전달받아 Factory에서 도메인 SearchCriteria로 변환됩니다.
 *
 * @param legacyUserId 레거시 사용자 ID (nullable)
 * @param memberId 회원 ID (nullable)
 * @param lastOrderId 커서 페이징용 마지막 주문 ID
 * @param size 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record AvailableReviewSearchParams(
        Long legacyUserId, Long memberId, Long lastOrderId, Integer size) {

    private static final int DEFAULT_SIZE = 20;

    public int sizeOrDefault() {
        return size != null ? size : DEFAULT_SIZE;
    }
}
