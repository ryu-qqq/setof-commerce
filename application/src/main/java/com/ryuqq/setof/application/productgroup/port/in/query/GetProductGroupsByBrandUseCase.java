package com.ryuqq.setof.application.productgroup.port.in.query;

import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;

/**
 * GetProductGroupsByBrandUseCase - 브랜드별 상품그룹 조회 UseCase.
 *
 * <p>GET /api/v1/product/group/brand/{brandId}
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetProductGroupsByBrandUseCase {
    ProductGroupSliceResult execute(Long brandId, int pageSize);
}
