package com.ryuqq.setof.storage.legacy.productgroup.adapter;

import com.ryuqq.setof.application.stock.port.out.query.StockQueryPort;
import com.ryuqq.setof.storage.legacy.productgroup.entity.LegacyProductStockEntity;
import com.ryuqq.setof.storage.legacy.productgroup.repository.LegacyProductStockJpaRepository;
import org.springframework.stereotype.Component;

/**
 * LegacyStockQueryAdapter - Legacy product_stock 재고 조회 어댑터.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyStockQueryAdapter implements StockQueryPort {

    private final LegacyProductStockJpaRepository repository;

    public LegacyStockQueryAdapter(LegacyProductStockJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public int getQuantity(long productId) {
        LegacyProductStockEntity entity =
                repository
                        .findById(productId)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "재고 정보 없음: productId=" + productId));
        return entity.getStockQuantity();
    }
}
