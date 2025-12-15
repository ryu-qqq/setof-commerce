package com.ryuqq.setof.application.productstock.service.command;

import com.ryuqq.setof.application.productstock.dto.command.InitializeStockCommand;
import com.ryuqq.setof.application.productstock.factory.command.ProductStockCommandFactory;
import com.ryuqq.setof.application.productstock.manager.command.ProductStockPersistenceManager;
import com.ryuqq.setof.application.productstock.port.in.command.InitializeStockUseCase;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import org.springframework.stereotype.Service;

/**
 * 재고 초기화 서비스
 *
 * <p>신규 상품의 재고를 초기화합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class InitializeStockService implements InitializeStockUseCase {

    private final ProductStockCommandFactory productStockCommandFactory;
    private final ProductStockPersistenceManager productStockPersistenceManager;

    public InitializeStockService(
            ProductStockCommandFactory productStockCommandFactory,
            ProductStockPersistenceManager productStockPersistenceManager) {
        this.productStockCommandFactory = productStockCommandFactory;
        this.productStockPersistenceManager = productStockPersistenceManager;
    }

    @Override
    public Long execute(InitializeStockCommand command) {
        ProductStock productStock = productStockCommandFactory.createProductStock(command);
        return productStockPersistenceManager.persist(productStock);
    }
}
