package com.ryuqq.setof.application.productstock.manager.command;

import com.ryuqq.setof.application.productstock.port.out.command.ProductStockPersistencePort;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductStock Persistence Manager
 *
 * <p>재고 저장 트랜잭션 경계 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductStockPersistenceManager {

    private final ProductStockPersistencePort productStockPersistencePort;

    public ProductStockPersistenceManager(ProductStockPersistencePort productStockPersistencePort) {
        this.productStockPersistencePort = productStockPersistencePort;
    }

    /**
     * 신규 재고 저장
     *
     * @param productStock 저장할 재고
     * @return 저장된 재고 ID
     */
    @Transactional
    public Long persist(ProductStock productStock) {
        return productStockPersistencePort.save(productStock);
    }

    /**
     * 재고 업데이트
     *
     * @param productStock 업데이트할 재고
     */
    @Transactional
    public void update(ProductStock productStock) {
        productStockPersistencePort.update(productStock);
    }
}
