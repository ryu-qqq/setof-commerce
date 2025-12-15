package com.ryuqq.setof.application.productstock.service.command;

import com.ryuqq.setof.application.productstock.dto.command.RestoreStockCommand;
import com.ryuqq.setof.application.productstock.factory.command.ProductStockCommandFactory;
import com.ryuqq.setof.application.productstock.manager.command.ProductStockPersistenceManager;
import com.ryuqq.setof.application.productstock.manager.query.ProductStockReadManager;
import com.ryuqq.setof.application.productstock.port.in.command.RestoreStockUseCase;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import com.ryuqq.setof.domain.productstock.exception.StockConcurrentModificationException;
import com.ryuqq.setof.domain.productstock.exception.StockOptimisticLockException;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 재고 복원 서비스
 *
 * <p>주문 취소 등에 의해 재고를 복원합니다.
 *
 * <p>낙관적 락 기반 동시성 제어:
 *
 * <ul>
 *   <li>StockOptimisticLockException 발생 시 자동 재시도
 *   <li>최대 재시도 횟수 초과 시 StockConcurrentModificationException 발생
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RestoreStockService implements RestoreStockUseCase {

    private static final int MAX_RETRY_COUNT = 3;

    private final ProductStockReadManager productStockReadManager;
    private final ProductStockPersistenceManager productStockPersistenceManager;
    private final ProductStockCommandFactory productStockCommandFactory;

    public RestoreStockService(
            ProductStockReadManager productStockReadManager,
            ProductStockPersistenceManager productStockPersistenceManager,
            ProductStockCommandFactory productStockCommandFactory) {
        this.productStockReadManager = productStockReadManager;
        this.productStockPersistenceManager = productStockPersistenceManager;
        this.productStockCommandFactory = productStockCommandFactory;
    }

    @Override
    public void execute(RestoreStockCommand command) {
        int retryCount = 0;

        while (retryCount < MAX_RETRY_COUNT) {
            try {
                executeWithOptimisticLock(command);
                return;
            } catch (StockOptimisticLockException ex) {
                retryCount++;
                if (retryCount >= MAX_RETRY_COUNT) {
                    throw new StockConcurrentModificationException(command.productId(), retryCount);
                }
            }
        }
    }

    private void executeWithOptimisticLock(RestoreStockCommand command) {
        ProductStock productStock = productStockReadManager.findByProductId(command.productId());

        Instant now = productStockCommandFactory.now();
        ProductStock restored = productStock.restore(command.quantity(), now);

        productStockPersistenceManager.update(restored);
    }
}
