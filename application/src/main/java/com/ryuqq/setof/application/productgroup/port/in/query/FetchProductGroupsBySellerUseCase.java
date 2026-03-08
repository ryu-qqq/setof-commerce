package com.ryuqq.setof.application.productgroup.port.in.query;

import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;

/**
 * FetchProductGroupsBySellerUseCase - 셀러별 상품그룹 조회 UseCase.
 *
 * <p>GET /api/v1/product/group/seller/{sellerId}
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface FetchProductGroupsBySellerUseCase {
    ProductGroupSliceResult execute(Long sellerId, int pageSize);
}
