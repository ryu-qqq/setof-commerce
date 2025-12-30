package com.ryuqq.setof.adapter.out.persistence.order.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.order.entity.OrderItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.order.entity.QOrderItemJpaEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * OrderItemQueryDslRepository - OrderItem QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 및 벌크 삭제 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findByOrderId(UUID orderId): 주문 ID로 주문 항목 목록 조회
 *   <li>deleteByOrderId(UUID orderId): 주문 ID로 주문 항목 삭제
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>Join 절대 금지
 *   <li>비즈니스 로직 금지
 *   <li>Mapper 호출 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class OrderItemQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QOrderItemJpaEntity qOrderItem = QOrderItemJpaEntity.orderItemJpaEntity;

    public OrderItemQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 주문 ID로 모든 OrderItem 조회
     *
     * @param orderId 주문 UUID
     * @return OrderItemJpaEntity 목록
     */
    public List<OrderItemJpaEntity> findByOrderId(UUID orderId) {
        return queryFactory.selectFrom(qOrderItem).where(qOrderItem.orderId.eq(orderId)).fetch();
    }

    /**
     * 주문 ID로 모든 OrderItem 삭제
     *
     * @param orderId 주문 UUID
     * @return 삭제된 건수
     */
    public long deleteByOrderId(UUID orderId) {
        return queryFactory.delete(qOrderItem).where(qOrderItem.orderId.eq(orderId)).execute();
    }
}
