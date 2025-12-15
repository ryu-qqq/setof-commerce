package com.ryuqq.setof.application.seller.dto.query;

/**
 * Seller Search Query
 *
 * <p>셀러 목록 검색 조건을 담는 순수한 불변 객체
 *
 * @param sellerName 셀러명 (LIKE 검색)
 * @param approvalStatus 승인 상태 (PENDING, APPROVED, REJECTED, SUSPENDED)
 * @param page 페이지 번호 (0-based)
 * @param size 페이지 크기
 * @author development-team
 * @since 1.0.0
 */
public record SellerSearchQuery(String sellerName, String approvalStatus, int page, int size) {

    /** Compact Constructor - 기본값 처리 */
    public SellerSearchQuery {
        page = Math.max(page, 0);
        size = (size <= 0 || size > 100) ? 20 : size;
    }
}
