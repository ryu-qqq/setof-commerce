package com.ryuqq.setof.application.productstock.port.in.query;

import com.ryuqq.setof.application.productstock.dto.response.ProductStockResponse;
import java.util.List;

/**
 * 재고 조회 UseCase
 *
 * <p>상품의 재고 정보를 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetProductStockUseCase {

    /**
     * 상품 ID로 재고 조회
     *
     * @param productId 상품 ID
     * @return 재고 응답
     */
    ProductStockResponse execute(Long productId);

    /**
     * 여러 상품의 재고 일괄 조회
     *
     * @param productIds 상품 ID 목록
     * @return 재고 응답 목록
     */
    List<ProductStockResponse> execute(List<Long> productIds);
}
