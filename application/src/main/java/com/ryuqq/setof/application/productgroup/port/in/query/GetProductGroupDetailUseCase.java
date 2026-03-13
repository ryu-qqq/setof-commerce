package com.ryuqq.setof.application.productgroup.port.in.query;

import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupDetailResult;

/**
 * GetProductGroupDetailUseCase - 상품그룹 단건 상세 조회 UseCase.
 *
 * <p>GET /api/v1/product/group/{productGroupId}
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetProductGroupDetailUseCase {
    ProductGroupDetailResult execute(Long productGroupId);
}
