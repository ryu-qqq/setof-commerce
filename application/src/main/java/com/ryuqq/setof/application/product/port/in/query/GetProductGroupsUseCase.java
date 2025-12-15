package com.ryuqq.setof.application.product.port.in.query;

import com.ryuqq.setof.application.product.dto.query.ProductGroupSearchQuery;
import com.ryuqq.setof.application.product.dto.response.ProductGroupSummaryResponse;
import java.util.List;

/**
 * Get ProductGroups UseCase (Query)
 *
 * <p>상품그룹 목록 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetProductGroupsUseCase {

    /**
     * 상품그룹 목록 조회
     *
     * @param query 검색 조건
     * @return 상품그룹 요약 목록
     */
    List<ProductGroupSummaryResponse> execute(ProductGroupSearchQuery query);

    /**
     * 상품그룹 총 개수 조회
     *
     * @param query 검색 조건
     * @return 총 개수
     */
    long count(ProductGroupSearchQuery query);
}
