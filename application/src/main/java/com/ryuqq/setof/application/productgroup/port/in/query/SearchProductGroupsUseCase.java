package com.ryuqq.setof.application.productgroup.port.in.query;

import com.ryuqq.setof.application.productgroup.dto.query.ProductGroupSearchParams;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;

/**
 * SearchProductGroupsUseCase - 키워드 기반 상품그룹 검색 UseCase.
 *
 * <p>GET /api/v1/search (MySQL ngram FULLTEXT 검색 + 커서 페이징)
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface SearchProductGroupsUseCase {
    ProductGroupSliceResult execute(ProductGroupSearchParams params);
}
