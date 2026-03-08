package com.ryuqq.setof.domain.qna.query;

import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;

/**
 * ProductQnaSearchCriteria - 상품 Q&A 오프셋 기반 검색 조건.
 *
 * <p>DOM-CRI-001: Record + of() 팩토리 메서드.
 *
 * <p>DOM-CRI-002: QueryContext 공통 VO 사용 (오프셋 페이징).
 *
 * @param productGroupId 상품그룹 ID
 * @param queryContext 정렬 + 오프셋 페이징 컨텍스트
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductQnaSearchCriteria(
        ProductGroupId productGroupId, QueryContext<QnaSortKey> queryContext) {

    public ProductQnaSearchCriteria {
        if (productGroupId == null) {
            throw new IllegalArgumentException("productGroupId must not be null");
        }
        if (queryContext == null) {
            queryContext = QueryContext.defaultOf(QnaSortKey.defaultKey());
        }
    }

    public static ProductQnaSearchCriteria of(
            ProductGroupId productGroupId, QueryContext<QnaSortKey> queryContext) {
        return new ProductQnaSearchCriteria(productGroupId, queryContext);
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

    public long productGroupIdValue() {
        return productGroupId.value();
    }
}
