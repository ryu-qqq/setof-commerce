package com.ryuqq.setof.application.productstock.service.command;

import com.ryuqq.setof.application.common.port.out.DistributedLockPort;
import com.ryuqq.setof.application.productstock.dto.command.SetStockCommand;
import com.ryuqq.setof.application.productstock.factory.command.ProductStockCommandFactory;
import com.ryuqq.setof.application.productstock.manager.command.ProductStockPersistenceManager;
import com.ryuqq.setof.application.productstock.manager.query.ProductStockReadManager;
import com.ryuqq.setof.application.productstock.port.in.command.SetStockUseCase;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import com.ryuqq.setof.domain.productstock.exception.StockLockAcquisitionException;
import com.ryuqq.setof.domain.productstock.vo.StockLockKey;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

/**
 * 재고 설정 서비스
 *
 * <p>관리자가 상품의 재고 수량을 직접 설정합니다.
 *
 * <p>분산락 기반 동시성 제어:
 *
 * <ul>
 *   <li>관리자 작업은 데이터 정합성이 중요하므로 분산락 사용
 *   <li>Lock 획득 대기 시간: 10초
 *   <li>Lock 유지 시간: 30초
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class SetStockService implements SetStockUseCase {

    private static final long LOCK_WAIT_TIME = 10;
    private static final long LOCK_LEASE_TIME = 30;

    private final ProductStockReadManager productStockReadManager;
    private final ProductStockPersistenceManager productStockPersistenceManager;
    private final ProductStockCommandFactory productStockCommandFactory;
    private final DistributedLockPort distributedLockPort;

    public SetStockService(
            ProductStockReadManager productStockReadManager,
            ProductStockPersistenceManager productStockPersistenceManager,
            ProductStockCommandFactory productStockCommandFactory,
            DistributedLockPort distributedLockPort) {
        this.productStockReadManager = productStockReadManager;
        this.productStockPersistenceManager = productStockPersistenceManager;
        this.productStockCommandFactory = productStockCommandFactory;
        this.distributedLockPort = distributedLockPort;
    }

    @Override
    public void execute(SetStockCommand command) {
        StockLockKey lockKey = new StockLockKey(command.productId());

        boolean acquired =
                distributedLockPort.tryLock(
                        lockKey, LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS);

        if (!acquired) {
            throw new StockLockAcquisitionException(command.productId());
        }

        try {
            executeWithLock(command);
        } finally {
            distributedLockPort.unlock(lockKey);
        }
    }

    private void executeWithLock(SetStockCommand command) {
        ProductStock productStock = productStockReadManager.findByProductId(command.productId());

        Instant now = productStockCommandFactory.now();
        ProductStock updated = productStock.setQuantity(command.quantity(), now);

        productStockPersistenceManager.update(updated);
    }
}
