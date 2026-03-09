package com.ryuqq.setof.storage.legacy.composite.mileage.repository;

import static com.ryuqq.setof.storage.legacy.mileage.entity.QLegacyMileageEntity.legacyMileageEntity;
import static com.ryuqq.setof.storage.legacy.mileage.entity.QLegacyMileageHistoryEntity.legacyMileageHistoryEntity;
import static com.ryuqq.setof.storage.legacy.mileage.entity.QLegacyOrderSnapshotMileageDetailEntity.legacyOrderSnapshotMileageDetailEntity;
import static com.ryuqq.setof.storage.legacy.mileage.entity.QLegacyOrderSnapshotMileageEntity.legacyOrderSnapshotMileageEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.mileage.dto.query.LegacyMileageHistorySearchCondition;
import com.ryuqq.setof.storage.legacy.composite.mileage.condition.LegacyWebMileageCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.mileage.dto.LegacyWebMileageHistoryQueryDto;
import com.ryuqq.setof.storage.legacy.composite.mileage.dto.LegacyWebUserMileageQueryDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * 레거시 Web Mileage Composite 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * <p>레거시 복잡한 조인 구조 (mileage → mileage_history → order_snapshot_*) 처리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebMileageCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebMileageCompositeConditionBuilder conditionBuilder;

    public LegacyWebMileageCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebMileageCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 마일리지 이력 조회.
     *
     * <p>레거시 fetchMileageHistories 대응.
     *
     * <p>4개 테이블 조인 (mileage, mileage_history, order_snapshot_mileage_detail,
     * order_snapshot_mileage).
     *
     * <p>복합 WHERE 조건 처리: targetId == 0 OR (유효 주문/결제 OR 주문/결제 없음)
     *
     * @param condition 검색 조건
     * @return 마일리지 이력 목록
     */
    public List<LegacyWebMileageHistoryQueryDto> fetchMileageHistories(
            LegacyMileageHistorySearchCondition condition) {

        return queryFactory
                .select(createMileageHistoryProjection())
                .from(legacyMileageEntity)
                .innerJoin(legacyMileageHistoryEntity)
                .on(conditionBuilder.mileageHistoryJoinCondition())
                .leftJoin(legacyOrderSnapshotMileageDetailEntity)
                .on(conditionBuilder.orderSnapshotDetailJoinCondition())
                .leftJoin(legacyOrderSnapshotMileageEntity)
                .on(
                        conditionBuilder.orderSnapshotJoinCondition(),
                        conditionBuilder.orderIdTargetIdJoinCondition())
                .where(
                        conditionBuilder.mileageUserIdEq(condition.userId()),
                        conditionBuilder.reasonIn(condition.reasons()),
                        createTargetIdOrOrderCondition())
                .groupBy(
                        legacyMileageHistoryEntity.id,
                        legacyMileageEntity.id,
                        legacyMileageEntity.title,
                        legacyOrderSnapshotMileageEntity.paymentId,
                        legacyOrderSnapshotMileageEntity.orderId,
                        legacyMileageHistoryEntity.changeAmount,
                        legacyMileageHistoryEntity.reason,
                        legacyMileageHistoryEntity.insertDate,
                        legacyMileageEntity.expirationDate)
                .orderBy(legacyMileageHistoryEntity.insertDate.desc())
                .offset(condition.getOffset())
                .limit(condition.pageSize())
                .distinct()
                .fetch();
    }

    /**
     * 마일리지 이력 카운트 조회.
     *
     * <p>레거시 fetchMileageHistoryCount 대응.
     *
     * @param condition 검색 조건
     * @return 이력 카운트
     */
    public long countMileageHistories(LegacyMileageHistorySearchCondition condition) {
        Long count =
                queryFactory
                        .select(legacyMileageHistoryEntity.count())
                        .from(legacyMileageHistoryEntity)
                        .where(
                                conditionBuilder.historyUserIdEq(condition.userId()),
                                conditionBuilder.reasonIn(condition.reasons()))
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 사용자 마일리지 목록 조회.
     *
     * <p>레거시 fetchUserMileageQueryInMyPage 대응.
     *
     * <p>잔여 마일리지 > 0 AND 활성 상태인 마일리지만 조회.
     *
     * @param userId 사용자 ID
     * @return 사용자 마일리지 목록
     */
    public List<LegacyWebUserMileageQueryDto> fetchUserMileages(long userId) {
        return queryFactory
                .select(createUserMileageProjection())
                .from(legacyMileageEntity)
                .where(
                        conditionBuilder.mileageUserIdEq(userId),
                        conditionBuilder.hasRemainingMileage(),
                        conditionBuilder.mileageActiveYn())
                .fetch();
    }

    private com.querydsl.core.types.ConstructorExpression<LegacyWebMileageHistoryQueryDto>
            createMileageHistoryProjection() {
        return Projections.constructor(
                LegacyWebMileageHistoryQueryDto.class,
                legacyMileageHistoryEntity.id,
                legacyMileageEntity.id,
                legacyMileageEntity.title,
                legacyOrderSnapshotMileageEntity.paymentId,
                legacyOrderSnapshotMileageEntity.orderId,
                legacyMileageHistoryEntity.changeAmount,
                legacyMileageHistoryEntity.reason,
                legacyMileageHistoryEntity.insertDate,
                legacyMileageEntity.expirationDate);
    }

    private com.querydsl.core.types.ConstructorExpression<LegacyWebUserMileageQueryDto>
            createUserMileageProjection() {
        return Projections.constructor(
                LegacyWebUserMileageQueryDto.class,
                legacyMileageEntity.userId,
                legacyMileageEntity.id,
                legacyMileageEntity.mileageAmount,
                legacyMileageEntity.usedMileageAmount,
                legacyMileageEntity.activeYn.stringValue(),
                legacyMileageEntity.issuedDate,
                legacyMileageEntity.expirationDate);
    }

    /**
     * 복합 조건: targetId == 0 OR (유효 주문/결제 존재 OR 주문/결제 없음).
     *
     * <p>레거시 쿼리의 복잡한 WHERE 조건 처리.
     *
     * @return BooleanExpression
     */
    private com.querydsl.core.types.dsl.BooleanExpression createTargetIdOrOrderCondition() {
        return conditionBuilder
                .targetIdIsZero()
                .or(
                        conditionBuilder
                                .hasValidOrderIdAndPaymentId()
                                .or(conditionBuilder.noOrderIdAndPaymentId()));
    }
}
