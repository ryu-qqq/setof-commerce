package com.ryuqq.setof.storage.legacy.composite.web.order.repository;

import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderEntity.legacyOrderEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.order.dto.query.LegacyOrderSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.order.condition.LegacyWebOrderCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.order.dto.LegacyWebOrderQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.order.dto.LegacyWebOrderStatusCountDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebOrderCompositeQueryDslRepository - 레거시 주문 Composite 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebOrderCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebOrderCompositeConditionBuilder conditionBuilder;

    public LegacyWebOrderCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebOrderCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 주문 목록 조회.
     *
     * <p>커서 기반 페이징, 날짜 범위, 상태 필터 지원.
     *
     * @param condition 검색 조건
     * @param limit 조회 개수
     * @return 주문 목록
     */
    public List<LegacyWebOrderQueryDto> fetchOrders(
            LegacyOrderSearchCondition condition, int limit) {
        return queryFactory
                .select(createProjection())
                .from(legacyOrderEntity)
                .where(
                        conditionBuilder.userIdEq(condition.userId()),
                        conditionBuilder.orderIdLt(condition.lastDomainId()),
                        conditionBuilder.insertDateBetween(
                                condition.startDate(), condition.endDate()),
                        conditionBuilder.orderStatusIn(condition.orderStatusList()))
                .orderBy(legacyOrderEntity.id.desc())
                .limit(limit)
                .distinct()
                .fetch();
    }

    /**
     * 주문 ID 목록 조회 (커서 기반 페이징용).
     *
     * @param condition 검색 조건
     * @param limit 조회 개수
     * @return 주문 ID 목록
     */
    public List<Long> fetchOrderIds(LegacyOrderSearchCondition condition, int limit) {
        return queryFactory
                .select(legacyOrderEntity.id)
                .from(legacyOrderEntity)
                .where(
                        conditionBuilder.userIdEq(condition.userId()),
                        conditionBuilder.orderIdLt(condition.lastDomainId()),
                        conditionBuilder.insertDateBetween(
                                condition.startDate(), condition.endDate()),
                        conditionBuilder.orderStatusIn(condition.orderStatusList()))
                .orderBy(legacyOrderEntity.id.desc())
                .limit(limit)
                .distinct()
                .fetch();
    }

    /**
     * 주문 개수 조회.
     *
     * @param condition 검색 조건
     * @return 주문 개수
     */
    public long countOrders(LegacyOrderSearchCondition condition) {
        Long count =
                queryFactory
                        .select(legacyOrderEntity.count())
                        .from(legacyOrderEntity)
                        .where(
                                conditionBuilder.userIdEq(condition.userId()),
                                conditionBuilder.orderIdLt(condition.lastDomainId()),
                                conditionBuilder.insertDateBetween(
                                        condition.startDate(), condition.endDate()),
                                conditionBuilder.orderStatusIn(condition.orderStatusList()))
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 상태별 주문 개수 조회 (최근 3개월).
     *
     * @param userId 사용자 ID
     * @param orderStatuses 주문 상태 목록
     * @return 상태별 주문 개수 목록
     */
    public List<LegacyWebOrderStatusCountDto> countOrdersByStatus(
            long userId, List<String> orderStatuses) {
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebOrderStatusCountDto.class,
                                legacyOrderEntity.orderStatus.stringValue(),
                                legacyOrderEntity.count()))
                .from(legacyOrderEntity)
                .where(
                        conditionBuilder.userIdEq(userId),
                        conditionBuilder.orderStatusIn(orderStatuses),
                        conditionBuilder.updateDateAfter3Months())
                .groupBy(legacyOrderEntity.orderStatus)
                .fetch();
    }

    /**
     * Projections.constructor()로 Projection 생성.
     *
     * <p>@QueryProjection 대신 사용.
     */
    private com.querydsl.core.types.ConstructorExpression<LegacyWebOrderQueryDto>
            createProjection() {
        return Projections.constructor(
                LegacyWebOrderQueryDto.class,
                legacyOrderEntity.id,
                legacyOrderEntity.paymentId,
                legacyOrderEntity.productId,
                legacyOrderEntity.sellerId,
                legacyOrderEntity.userId,
                legacyOrderEntity.orderAmount,
                legacyOrderEntity.orderStatus.stringValue(),
                legacyOrderEntity.quantity,
                legacyOrderEntity.reviewYn.stringValue(),
                legacyOrderEntity.insertDate,
                legacyOrderEntity.updateDate);
    }
}
