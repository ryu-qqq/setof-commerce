package com.ryuqq.setof.application.review.dto.query;

import java.util.UUID;

/**
 * Review Search Query
 *
 * <p>리뷰 목록 검색 조건을 담는 순수한 불변 객체
 *
 * @param productGroupId 상품 그룹 ID (상품별 리뷰 조회)
 * @param memberId 회원 ID (내가 작성한 리뷰 조회, UUID)
 * @param page 페이지 번호 (0-based)
 * @param size 페이지 크기
 * @author development-team
 * @since 1.0.0
 */
public record ReviewSearchQuery(Long productGroupId, UUID memberId, int page, int size) {

    /** Compact Constructor - 기본값 처리 */
    public ReviewSearchQuery {
        page = Math.max(page, 0);
        size = (size <= 0 || size > 100) ? 20 : size;
    }

    /**
     * 상품별 리뷰 조회용 쿼리 생성
     *
     * @param productGroupId 상품 그룹 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ReviewSearchQuery
     */
    public static ReviewSearchQuery forProduct(Long productGroupId, int page, int size) {
        return new ReviewSearchQuery(productGroupId, null, page, size);
    }

    /**
     * 사용자별 리뷰 조회용 쿼리 생성
     *
     * @param memberId 회원 ID (UUID)
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ReviewSearchQuery
     */
    public static ReviewSearchQuery forUser(UUID memberId, int page, int size) {
        return new ReviewSearchQuery(null, memberId, page, size);
    }
}
