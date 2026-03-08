package com.ryuqq.setof.application.cart.manager;

import com.ryuqq.setof.application.cart.port.out.query.CartStockQueryPort;
import com.ryuqq.setof.domain.cart.exception.InsufficientStockException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * CartStockReadManager - 장바구니 재고 검증 Manager.
 *
 * <p>레거시 product_stock 테이블에서 재고를 조회하고 검증합니다. 추후 Redis 기반 재고 시스템 구축 시 Port 구현체만 교체.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class CartStockReadManager {

    private final CartStockQueryPort stockQueryPort;

    public CartStockReadManager(CartStockQueryPort stockQueryPort) {
        this.stockQueryPort = stockQueryPort;
    }

    @Transactional(readOnly = true)
    public void validateStockAvailability(long productId, int requestedQuantity) {
        int stockQuantity = stockQueryPort.fetchStockQuantity(productId);
        if (stockQuantity < requestedQuantity) {
            throw new InsufficientStockException(productId, requestedQuantity, stockQuantity);
        }
    }
}
