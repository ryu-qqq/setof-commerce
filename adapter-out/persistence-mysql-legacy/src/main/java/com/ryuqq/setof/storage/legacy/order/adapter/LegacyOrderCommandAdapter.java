package com.ryuqq.setof.storage.legacy.order.adapter;

import com.ryuqq.setof.application.order.port.out.command.LegacyOrderCommandPort;
import com.ryuqq.setof.storage.legacy.order.entity.LegacyOrderEntity;
import com.ryuqq.setof.storage.legacy.order.entity.LegacyOrderStatus;
import com.ryuqq.setof.storage.legacy.order.repository.LegacyOrderJpaRepository;
import org.springframework.stereotype.Component;

/**
 * LegacyOrderCommandAdapter - Legacy orders 테이블 명령 어댑터.
 *
 * <p>JPA Dirty Checking으로 order_status를 업데이트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyOrderCommandAdapter implements LegacyOrderCommandPort {

    private final LegacyOrderJpaRepository repository;

    public LegacyOrderCommandAdapter(LegacyOrderJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public String updateOrderStatus(long orderId, String targetOrderStatus) {
        LegacyOrderEntity entity =
                repository
                        .findById(orderId)
                        .orElseThrow(
                                () -> new IllegalArgumentException("Order not found: " + orderId));
        String previousStatus = entity.getOrderStatus().name();
        entity.updateOrderStatus(LegacyOrderStatus.valueOf(targetOrderStatus));
        return previousStatus;
    }
}
