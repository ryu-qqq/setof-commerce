package com.ryuqq.setof.storage.legacy.order.adapter;

import com.ryuqq.setof.application.order.port.out.query.LegacyOrderQueryPort;
import com.ryuqq.setof.storage.legacy.order.entity.LegacyOrderEntity;
import com.ryuqq.setof.storage.legacy.order.repository.LegacyOrderJpaRepository;
import org.springframework.stereotype.Component;

/**
 * LegacyOrderQueryAdapter - Legacy orders 테이블 조회 어댑터 (Command 검증용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyOrderQueryAdapter implements LegacyOrderQueryPort {

    private final LegacyOrderJpaRepository repository;

    public LegacyOrderQueryAdapter(LegacyOrderJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public String fetchOrderStatus(long orderId) {
        return repository
                .findById(orderId)
                .map(entity -> entity.getOrderStatus().name())
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

    @Override
    public long fetchOrderUserId(long orderId) {
        return repository
                .findById(orderId)
                .map(LegacyOrderEntity::getUserId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }
}
