package com.ryuqq.setof.storage.legacy.cart.adapter;

import com.ryuqq.setof.application.cart.port.out.query.CartStockQueryPort;
import com.ryuqq.setof.storage.legacy.cart.repository.LegacyCartStockQueryDslRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * LegacyCartStockQueryAdapter - 레거시 DB 기반 장바구니 재고 조회 Adapter.
 *
 * <p>product_stock 테이블에서 재고를 조회합니다. 추후 Redis 기반 재고 시스템 구축 시 이 구현체를 교체합니다.
 *
 * <p>활성화 조건: persistence.legacy.cart.enabled=true
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.cart.enabled", havingValue = "true")
public class LegacyCartStockQueryAdapter implements CartStockQueryPort {

    private final LegacyCartStockQueryDslRepository repository;

    public LegacyCartStockQueryAdapter(LegacyCartStockQueryDslRepository repository) {
        this.repository = repository;
    }

    @Override
    public int fetchStockQuantity(long productId) {
        Integer quantity = repository.fetchStockQuantity(productId);
        return quantity != null ? quantity : 0;
    }
}
