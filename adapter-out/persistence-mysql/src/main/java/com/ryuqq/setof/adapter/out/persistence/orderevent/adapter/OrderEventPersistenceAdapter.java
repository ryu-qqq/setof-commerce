package com.ryuqq.setof.adapter.out.persistence.orderevent.adapter;

import com.ryuqq.setof.adapter.out.persistence.orderevent.entity.OrderEventJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.orderevent.mapper.OrderEventJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.orderevent.repository.OrderEventJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.orderevent.repository.OrderEventQueryDslRepository;
import com.ryuqq.setof.application.orderevent.port.out.command.OrderEventPersistencePort;
import com.ryuqq.setof.application.orderevent.port.out.query.OrderEventQueryPort;
import com.ryuqq.setof.domain.order.vo.OrderId;
import com.ryuqq.setof.domain.orderevent.aggregate.OrderEvent;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * OrderEventPersistenceAdapter - OrderEvent 영속성 어댑터
 *
 * <p>Application Layer의 Port를 구현하여 영속성 계층과 연결합니다.
 *
 * <p><strong>Repository 분리:</strong>
 *
 * <ul>
 *   <li>JpaRepository: 표준 CRUD (save, delete)
 *   <li>QueryDslRepository: 커스텀 쿼리 (findBy*, orderBy)
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class OrderEventPersistenceAdapter
        implements OrderEventPersistencePort, OrderEventQueryPort {

    private final OrderEventJpaRepository orderEventJpaRepository;
    private final OrderEventQueryDslRepository orderEventQueryDslRepository;
    private final OrderEventJpaEntityMapper orderEventJpaEntityMapper;

    public OrderEventPersistenceAdapter(
            OrderEventJpaRepository orderEventJpaRepository,
            OrderEventQueryDslRepository orderEventQueryDslRepository,
            OrderEventJpaEntityMapper orderEventJpaEntityMapper) {
        this.orderEventJpaRepository = orderEventJpaRepository;
        this.orderEventQueryDslRepository = orderEventQueryDslRepository;
        this.orderEventJpaEntityMapper = orderEventJpaEntityMapper;
    }

    @Override
    public OrderEvent persist(OrderEvent orderEvent) {
        OrderEventJpaEntity entity = orderEventJpaEntityMapper.toEntity(orderEvent);
        OrderEventJpaEntity saved = orderEventJpaRepository.save(entity);
        return orderEventJpaEntityMapper.toDomain(saved);
    }

    @Override
    public List<OrderEvent> findByOrderId(OrderId orderId) {
        return orderEventQueryDslRepository
                .findByOrderIdOrderByCreatedAtAsc(orderId.value().toString())
                .stream()
                .map(orderEventJpaEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<OrderEvent> findByOrderIdDesc(OrderId orderId) {
        return orderEventQueryDslRepository
                .findByOrderIdOrderByCreatedAtDesc(orderId.value().toString())
                .stream()
                .map(orderEventJpaEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<OrderEvent> findBySourceId(String eventSource, String sourceId) {
        return orderEventQueryDslRepository
                .findByEventSourceAndSourceIdOrderByCreatedAtAsc(eventSource, sourceId)
                .stream()
                .map(orderEventJpaEntityMapper::toDomain)
                .toList();
    }
}
