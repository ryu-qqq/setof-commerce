package com.ryuqq.setof.domain.review.query;

import com.ryuqq.setof.domain.common.vo.QueryContext;

/**
 * 상품그룹 리뷰 오프셋 기반 검색 조건.
 *
 * <p>상품그룹별 리뷰 목록을 조회하기 위한 검색 조건입니다.
 *
 * @param productGroupId 상품그룹 ID
 * @param queryContext 정렬 + 오프셋 페이징 컨텍스트
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductGroupReviewSearchCriteria(
        long productGroupId, QueryContext<ProductGroupReviewSortKey> queryContext) {

    public ProductGroupReviewSearchCriteria {
        if (queryContext == null) {
            queryContext = QueryContext.defaultOf(ProductGroupReviewSortKey.defaultKey());
        }
    }

    public static ProductGroupReviewSearchCriteria of(
            long productGroupId, QueryContext<ProductGroupReviewSortKey> queryContext) {
        return new ProductGroupReviewSearchCriteria(productGroupId, queryContext);
    }

    public int size() {
        return queryContext.size();
    }

    public long offset() {
        return queryContext.offset();
    }

    public int page() {
        return queryContext.page();
    }

    public ProductGroupReviewSortKey sortKey() {
        return queryContext.sortKey();
    }
}
