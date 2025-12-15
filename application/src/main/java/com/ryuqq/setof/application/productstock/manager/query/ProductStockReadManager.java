package com.ryuqq.setof.application.productstock.manager.query;

import com.ryuqq.setof.application.productstock.port.out.query.ProductStockQueryPort;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductStock Read Manager
 *
 * <p>재고 조회 트랜잭션 경계 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductStockReadManager {

    private final ProductStockQueryPort productStockQueryPort;

    public ProductStockReadManager(ProductStockQueryPort productStockQueryPort) {
        this.productStockQueryPort = productStockQueryPort;
    }

    /**
     * 상품 ID로 재고 조회
     *
     * @param productId 상품 ID
     * @return 재고
     * @throws IllegalArgumentException 재고가 없는 경우
     */
    @Transactional(readOnly = true)
    public ProductStock findByProductId(Long productId) {
        return productStockQueryPort
                .findByProductId(productId)
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "해당 상품의 재고 정보가 없습니다. productId: " + productId));
    }

    /**
     * 여러 상품의 재고 일괄 조회
     *
     * @param productIds 상품 ID 목록
     * @return 재고 목록
     */
    @Transactional(readOnly = true)
    public List<ProductStock> findByProductIds(List<Long> productIds) {
        return productStockQueryPort.findByProductIds(productIds);
    }

    /**
     * 재고 ID로 조회
     *
     * @param productStockId 재고 ID
     * @return 재고
     * @throws IllegalArgumentException 재고가 없는 경우
     */
    @Transactional(readOnly = true)
    public ProductStock findById(Long productStockId) {
        return productStockQueryPort
                .findById(productStockId)
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "해당 재고 정보가 없습니다. productStockId: " + productStockId));
    }
}
