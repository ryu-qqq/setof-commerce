package com.ryuqq.setof.application.product.port.in.query;

import com.ryuqq.setof.application.product.dto.response.FullProductResponse;

/**
 * 전체 상품 조회 UseCase
 *
 * <p>상품그룹 + SKU + 이미지 + 설명 + 고시 + 재고 통합 조회
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetFullProductUseCase {

    /**
     * 상품 전체 조회 (모든 관련 Aggregate 포함)
     *
     * @param productGroupId 상품그룹 ID
     * @return 전체 상품 정보
     */
    FullProductResponse getFullProduct(Long productGroupId);
}
