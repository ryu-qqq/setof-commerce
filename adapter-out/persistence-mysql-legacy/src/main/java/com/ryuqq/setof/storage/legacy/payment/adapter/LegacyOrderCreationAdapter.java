package com.ryuqq.setof.storage.legacy.payment.adapter;

import com.ryuqq.setof.application.payment.port.out.command.OrderCreationPort;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.aggregate.OrderItem;
import com.ryuqq.setof.storage.legacy.order.entity.LegacyOrderEntity;
import com.ryuqq.setof.storage.legacy.order.entity.LegacyOrderStatus;
import com.ryuqq.setof.storage.legacy.order.repository.LegacyOrderJpaRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyOrderCreationAdapter - Order 도메인 → Legacy 스키마 영속 어댑터.
 *
 * <p>Order에서 paymentId, userId, OrderItems를 추출하여 Legacy orders 테이블에 저장합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyOrderCreationAdapter implements OrderCreationPort {

    private final LegacyOrderJpaRepository orderRepository;

    public LegacyOrderCreationAdapter(LegacyOrderJpaRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Long> persist(Order order) {
        List<LegacyOrderEntity> orderEntities =
                order.orderItems().stream()
                        .map(
                                item ->
                                        toOrderEntity(
                                                item, order.paymentId(), order.legacyUserIdValue()))
                        .toList();

        List<LegacyOrderEntity> savedOrders = orderRepository.saveAll(orderEntities);

        return savedOrders.stream().map(LegacyOrderEntity::getId).toList();
    }

    private LegacyOrderEntity toOrderEntity(OrderItem item, long paymentId, long userId) {
        return LegacyOrderEntity.create(
                paymentId,
                item.productIdValue(),
                item.sellerIdValue(),
                userId,
                item.orderAmount(),
                item.quantityValue(),
                LegacyOrderStatus.ORDER_PROCESSING);
    }
}
