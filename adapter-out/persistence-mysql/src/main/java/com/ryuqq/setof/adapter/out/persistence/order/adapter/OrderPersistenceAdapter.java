package com.ryuqq.setof.adapter.out.persistence.order.adapter;

import com.ryuqq.setof.adapter.out.persistence.order.entity.OrderItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.order.entity.OrderJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.order.mapper.OrderJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.order.repository.OrderItemJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.order.repository.OrderItemQueryDslRepository;
import com.ryuqq.setof.adapter.out.persistence.order.repository.OrderJpaRepository;
import com.ryuqq.setof.application.order.port.out.command.OrderPersistencePort;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.vo.OrderId;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * OrderPersistenceAdapter - Order Command Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, Order 저장 요청을 JpaRepository에 위임합니다.
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>OrderJpaEntity와 OrderItemJpaEntity 별도 저장
 *   <li>JPA Cascade 사용 금지
 * </ul>
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>Order 저장 (persist)
 *   <li>OrderItems 별도 저장
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>조회 로직 금지 (QueryAdapter로 분리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class OrderPersistenceAdapter implements OrderPersistencePort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;
    private final OrderItemQueryDslRepository orderItemQueryDslRepository;
    private final OrderJpaEntityMapper orderJpaEntityMapper;

    public OrderPersistenceAdapter(
            OrderJpaRepository orderJpaRepository,
            OrderItemJpaRepository orderItemJpaRepository,
            OrderItemQueryDslRepository orderItemQueryDslRepository,
            OrderJpaEntityMapper orderJpaEntityMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderItemJpaRepository = orderItemJpaRepository;
        this.orderItemQueryDslRepository = orderItemQueryDslRepository;
        this.orderJpaEntityMapper = orderJpaEntityMapper;
    }

    /**
     * Order 저장 (생성/수정)
     *
     * <p>Order와 Items를 별도로 저장합니다. (Long FK 전략)
     *
     * @param order Order 도메인
     * @return 저장된 OrderId
     */
    @Override
    public OrderId persist(Order order) {
        UUID orderId = order.id().value();

        // Parent Entity 저장
        OrderJpaEntity entity = orderJpaEntityMapper.toEntity(order);
        orderJpaRepository.save(entity);

        // 기존 Items 삭제 (Update 케이스 대응, QueryDslRepository 사용)
        orderItemQueryDslRepository.deleteByOrderId(orderId);

        // 새 Items 저장
        List<OrderItemJpaEntity> itemEntities =
                orderJpaEntityMapper.toItemEntities(orderId, order.items());
        orderItemJpaRepository.saveAll(itemEntities);

        return OrderId.of(orderId);
    }
}
