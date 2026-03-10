package com.ryuqq.setof.storage.legacy.productgroup.adapter;

import com.ryuqq.setof.application.stock.port.out.command.StockCommandPort;
import com.ryuqq.setof.storage.legacy.productgroup.entity.LegacyProductStockEntity;
import com.ryuqq.setof.storage.legacy.productgroup.repository.LegacyProductStockJpaRepository;
import org.springframework.stereotype.Component;

/**
 * LegacyStockCommandAdapter - Legacy product_stock 재고 차감/복원 어댑터.
 *
 * <p>JPA Dirty Checking으로 stock_quantity를 업데이트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyStockCommandAdapter implements StockCommandPort {

    private final LegacyProductStockJpaRepository repository;

    public LegacyStockCommandAdapter(LegacyProductStockJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void deduct(long productId, int quantity) {
        LegacyProductStockEntity entity = findOrThrow(productId);
        entity.deduct(quantity);
    }

    @Override
    public void restore(long productId, int quantity) {
        LegacyProductStockEntity entity = findOrThrow(productId);
        entity.restore(quantity);
    }

    private LegacyProductStockEntity findOrThrow(long productId) {
        return repository
                .findById(productId)
                .orElseThrow(
                        () -> new IllegalArgumentException("재고 정보 없음: productId=" + productId));
    }
}
