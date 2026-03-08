package com.ryuqq.setof.application.productgroup.port.in.query;

import com.ryuqq.setof.application.productgroup.dto.query.ProductGroupSearchParams;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;

/**
 * FetchProductGroupsUseCase - 상품그룹 커서 페이징 목록 조회 UseCase.
 *
 * <p>GET /api/v1/products/group
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface FetchProductGroupsUseCase {
    ProductGroupSliceResult execute(ProductGroupSearchParams params);
}
