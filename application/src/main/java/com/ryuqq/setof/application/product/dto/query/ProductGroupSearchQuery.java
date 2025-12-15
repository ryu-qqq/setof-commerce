package com.ryuqq.setof.application.product.dto.query;

/**
 * ProductGroup Search Query
 *
 * <p>상품그룹 검색 조건을 담는 불변 객체
 *
 * @param sellerId 셀러 ID (nullable)
 * @param categoryId 카테고리 ID (nullable)
 * @param brandId 브랜드 ID (nullable)
 * @param name 상품그룹명 (LIKE 검색, nullable)
 * @param status 상태 (ACTIVE, INACTIVE, nullable)
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 * @author development-team
 * @since 1.0.0
 */
public record ProductGroupSearchQuery(
        Long sellerId,
        Long categoryId,
        Long brandId,
        String name,
        String status,
        int page,
        int size) {

    /** 기본 페이지 크기 */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 페이지 오프셋 계산
     *
     * @return 오프셋 값
     */
    public int offset() {
        return page * size;
    }

    /**
     * 기본값으로 Query 생성
     *
     * @return 기본 검색 조건
     */
    public static ProductGroupSearchQuery defaultQuery() {
        return new ProductGroupSearchQuery(null, null, null, null, null, 0, DEFAULT_PAGE_SIZE);
    }

    /**
     * 셀러 ID로 검색 Query 생성
     *
     * @param sellerId 셀러 ID
     * @param page 페이지
     * @param size 크기
     * @return 검색 조건
     */
    public static ProductGroupSearchQuery bySellerId(Long sellerId, int page, int size) {
        return new ProductGroupSearchQuery(sellerId, null, null, null, null, page, size);
    }
}
