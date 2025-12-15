package com.ryuqq.setof.application.productstock.port.out.command;

import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;

/**
 * ProductStock Persistence Port (Out)
 *
 * <p>재고 저장소 인터페이스
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductStockPersistencePort {

    /**
     * 재고 저장
     *
     * @param productStock 저장할 재고
     * @return 저장된 재고 ID
     */
    Long save(ProductStock productStock);

    /**
     * 재고 업데이트
     *
     * @param productStock 업데이트할 재고
     */
    void update(ProductStock productStock);
}
