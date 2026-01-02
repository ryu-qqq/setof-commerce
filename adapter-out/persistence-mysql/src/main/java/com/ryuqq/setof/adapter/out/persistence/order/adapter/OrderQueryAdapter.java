package com.ryuqq.setof.adapter.out.persistence.order.adapter;

import com.ryuqq.setof.adapter.out.persistence.order.condition.OrderSearchCondition;
import com.ryuqq.setof.adapter.out.persistence.order.condition.OrderSearchCondition.SortField;
import com.ryuqq.setof.adapter.out.persistence.order.entity.OrderItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.order.entity.OrderJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.order.mapper.OrderJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.order.repository.OrderItemQueryDslRepository;
import com.ryuqq.setof.adapter.out.persistence.order.repository.OrderQueryDslRepository;
import com.ryuqq.setof.application.order.port.out.query.OrderQueryPort;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.query.criteria.OrderSearchCriteria;
import com.ryuqq.setof.domain.order.vo.OrderId;
import com.ryuqq.setof.domain.order.vo.OrderNumber;
import com.ryuqq.setof.domain.order.vo.OrderSortBy;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * OrderQueryAdapter - Order Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, Order 조회 요청을 QueryDslRepository에 위임하고 Mapper를 통해 Domain으로 변환하여 반환합니다.
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>OrderJpaEntity와 OrderItemJpaEntity 별도 조회
 *   <li>Mapper에 함께 전달하여 Domain 조립
 * </ul>
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>ID로 단건 조회 (findById)
 *   <li>주문 번호로 단건 조회 (findByOrderNumber)
 *   <li>Criteria 기반 목록 조회 (findByCriteria)
 *   <li>Domain Criteria → Persistence Condition 변환
 *   <li>Mapper를 통한 Entity → Domain 변환
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>저장/수정/삭제 금지 (PersistenceAdapter로 분리)
 *   <li>JPAQueryFactory 직접 사용 금지 (QueryDslRepository에서 처리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class OrderQueryAdapter implements OrderQueryPort {

    private final OrderQueryDslRepository orderQueryDslRepository;
    private final OrderItemQueryDslRepository orderItemQueryDslRepository;
    private final OrderJpaEntityMapper orderJpaEntityMapper;

    public OrderQueryAdapter(
            OrderQueryDslRepository orderQueryDslRepository,
            OrderItemQueryDslRepository orderItemQueryDslRepository,
            OrderJpaEntityMapper orderJpaEntityMapper) {
        this.orderQueryDslRepository = orderQueryDslRepository;
        this.orderItemQueryDslRepository = orderItemQueryDslRepository;
        this.orderJpaEntityMapper = orderJpaEntityMapper;
    }

    /**
     * ID로 Order 단건 조회
     *
     * @param orderId Order ID (Value Object)
     * @return Order Domain (Optional)
     */
    @Override
    public Optional<Order> findById(OrderId orderId) {
        Optional<OrderJpaEntity> entityOpt = orderQueryDslRepository.findById(orderId.value());

        if (entityOpt.isEmpty()) {
            return Optional.empty();
        }

        OrderJpaEntity entity = entityOpt.get();
        List<OrderItemJpaEntity> itemEntities =
                orderItemQueryDslRepository.findByOrderId(entity.getId());

        return Optional.of(orderJpaEntityMapper.toDomain(entity, itemEntities));
    }

    /**
     * 주문 번호로 Order 단건 조회
     *
     * @param orderNumber 주문 번호 (Value Object)
     * @return Order Domain (Optional)
     */
    @Override
    public Optional<Order> findByOrderNumber(OrderNumber orderNumber) {
        Optional<OrderJpaEntity> entityOpt =
                orderQueryDslRepository.findByOrderNumber(orderNumber.value());

        if (entityOpt.isEmpty()) {
            return Optional.empty();
        }

        OrderJpaEntity entity = entityOpt.get();
        List<OrderItemJpaEntity> itemEntities =
                orderItemQueryDslRepository.findByOrderId(entity.getId());

        return Optional.of(orderJpaEntityMapper.toDomain(entity, itemEntities));
    }

    /**
     * Legacy Order ID로 Order 단건 조회
     *
     * <p>V1 API 호환을 위한 Legacy ID 조회 메서드
     *
     * @param legacyOrderId Legacy Order ID (Long)
     * @return Order Domain (Optional)
     */
    @Override
    public Optional<Order> findByLegacyId(Long legacyOrderId) {
        Optional<OrderJpaEntity> entityOpt =
                orderQueryDslRepository.findByLegacyOrderId(legacyOrderId);

        if (entityOpt.isEmpty()) {
            return Optional.empty();
        }

        OrderJpaEntity entity = entityOpt.get();
        List<OrderItemJpaEntity> itemEntities =
                orderItemQueryDslRepository.findByOrderId(entity.getId());

        return Optional.of(orderJpaEntityMapper.toDomain(entity, itemEntities));
    }

    /**
     * Criteria 기반 주문 목록 조회 (커서 기반 페이징)
     *
     * <p>Domain Criteria → Persistence Condition 변환 후 Repository 호출
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return Order Domain 목록
     */
    @Override
    public List<Order> findByCriteria(OrderSearchCriteria criteria) {
        OrderSearchCondition condition = toCondition(criteria);
        List<OrderJpaEntity> entities = orderQueryDslRepository.findByCondition(condition);

        return entities.stream()
                .map(
                        entity -> {
                            List<OrderItemJpaEntity> itemEntities =
                                    orderItemQueryDslRepository.findByOrderId(entity.getId());
                            return orderJpaEntityMapper.toDomain(entity, itemEntities);
                        })
                .toList();
    }

    /**
     * Criteria 기반 주문 개수 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return 주문 개수
     */
    @Override
    public long countByCriteria(OrderSearchCriteria criteria) {
        OrderSearchCondition condition = toCondition(criteria);
        return orderQueryDslRepository.countByCondition(condition);
    }

    /**
     * 회원 ID와 상태별 주문 개수 조회
     *
     * @param memberId 회원 ID
     * @param statuses 조회할 상태 목록
     * @return 상태별 개수 Map
     */
    @Override
    public Map<String, Long> getOrderStatusCounts(String memberId, List<String> statuses) {
        return orderQueryDslRepository.countOrderStatusesByMemberId(memberId, statuses);
    }

    /**
     * Domain Criteria → Persistence Condition 변환
     *
     * <p>Adapter의 핵심 역할: Domain과 Persistence 간의 변환
     *
     * @param criteria Domain Layer의 검색 조건
     * @return Persistence Layer 전용 검색 조건
     */
    private OrderSearchCondition toCondition(OrderSearchCriteria criteria) {
        UUID lastOrderUuid = null;
        if (criteria.hasCursor()) {
            lastOrderUuid = UUID.fromString(criteria.lastOrderId());
        }

        SortField sortField = toSortField(criteria.effectiveSortBy());
        boolean sortAscending = isSortAscending(criteria.effectiveSortDirection());

        return OrderSearchCondition.of(
                criteria.memberId(),
                criteria.sellerId(),
                criteria.orderStatuses(),
                criteria.searchKeyword(),
                criteria.startDate(),
                criteria.endDate(),
                sortField,
                sortAscending,
                lastOrderUuid,
                criteria.fetchSize());
    }

    /**
     * Domain SortBy → Persistence SortField 변환
     *
     * @param sortBy Domain 정렬 필드
     * @return Persistence 정렬 필드
     */
    private SortField toSortField(OrderSortBy sortBy) {
        return switch (sortBy) {
            case ID -> SortField.ID;
            case ORDER_DATE -> SortField.ORDER_DATE;
            case CREATED_AT -> SortField.CREATED_AT;
            case UPDATED_AT -> SortField.UPDATED_AT;
            case TOTAL_AMOUNT -> SortField.TOTAL_AMOUNT;
        };
    }

    /**
     * 정렬 방향이 오름차순인지 확인
     *
     * @param sortDirection 정렬 방향
     * @return 오름차순이면 true
     */
    private boolean isSortAscending(SortDirection sortDirection) {
        return sortDirection == SortDirection.ASC;
    }
}
