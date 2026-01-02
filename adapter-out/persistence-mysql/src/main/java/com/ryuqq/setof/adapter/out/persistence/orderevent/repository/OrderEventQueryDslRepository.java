package com.ryuqq.setof.adapter.out.persistence.orderevent.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.orderevent.entity.OrderEventJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.orderevent.entity.QOrderEventJpaEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * OrderEventQueryDslRepository - 주문 이벤트 QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findByOrderIdOrderByCreatedAtDesc: 주문 ID로 이벤트 조회 (최신순)
 *   <li>findByOrderIdOrderByCreatedAtAsc: 주문 ID로 이벤트 조회 (오래된순)
 *   <li>findByEventSourceAndSourceIdOrderByCreatedAtAsc: 이벤트 출처로 조회
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
 * @since 2.0.0
 */
@Repository
public class OrderEventQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QOrderEventJpaEntity qOrderEvent =
            QOrderEventJpaEntity.orderEventJpaEntity;

    public OrderEventQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 주문 ID로 이벤트 목록 조회 (생성일시 내림차순)
     *
     * @param orderId 주문 ID
     * @return 이벤트 목록
     */
    public List<OrderEventJpaEntity> findByOrderIdOrderByCreatedAtDesc(String orderId) {
        return queryFactory
                .selectFrom(qOrderEvent)
                .where(qOrderEvent.orderId.eq(orderId))
                .orderBy(qOrderEvent.createdAt.desc())
                .fetch();
    }

    /**
     * 주문 ID로 이벤트 목록 조회 (생성일시 오름차순)
     *
     * @param orderId 주문 ID
     * @return 이벤트 목록
     */
    public List<OrderEventJpaEntity> findByOrderIdOrderByCreatedAtAsc(String orderId) {
        return queryFactory
                .selectFrom(qOrderEvent)
                .where(qOrderEvent.orderId.eq(orderId))
                .orderBy(qOrderEvent.createdAt.asc())
                .fetch();
    }

    /**
     * 이벤트 출처와 출처 ID로 이벤트 목록 조회
     *
     * @param eventSource 이벤트 출처
     * @param sourceId 출처 ID
     * @return 이벤트 목록
     */
    public List<OrderEventJpaEntity> findByEventSourceAndSourceIdOrderByCreatedAtAsc(
            String eventSource, String sourceId) {
        return queryFactory
                .selectFrom(qOrderEvent)
                .where(qOrderEvent.eventSource.eq(eventSource), qOrderEvent.sourceId.eq(sourceId))
                .orderBy(qOrderEvent.createdAt.asc())
                .fetch();
    }
}
