package com.ryuqq.setof.application.product.port.in.query;

import com.ryuqq.setof.application.product.dto.response.ProductGroupResponse;

/**
 * Get ProductGroup UseCase (Query)
 *
 * <p>상품그룹 단건 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetProductGroupUseCase {

    /**
     * 상품그룹 단건 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품그룹 응답
     */
    ProductGroupResponse execute(Long productGroupId);
}
