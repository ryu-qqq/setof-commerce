package com.ryuqq.setof.application.productstock.port.out.query;

import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import java.util.List;
import java.util.Optional;

/**
 * ProductStock Query Port (Out)
 *
 * <p>재고 조회 인터페이스
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductStockQueryPort {

    /**
     * 상품 ID로 재고 조회
     *
     * @param productId 상품 ID
     * @return 재고 (없으면 empty)
     */
    Optional<ProductStock> findByProductId(Long productId);

    /**
     * 여러 상품의 재고 일괄 조회
     *
     * @param productIds 상품 ID 목록
     * @return 재고 목록
     */
    List<ProductStock> findByProductIds(List<Long> productIds);

    /**
     * 재고 ID로 조회
     *
     * @param productStockId 재고 ID
     * @return 재고 (없으면 empty)
     */
    Optional<ProductStock> findById(Long productStockId);
}
