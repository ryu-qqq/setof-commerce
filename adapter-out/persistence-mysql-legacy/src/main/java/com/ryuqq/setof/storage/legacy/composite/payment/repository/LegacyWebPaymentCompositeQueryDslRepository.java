package com.ryuqq.setof.storage.legacy.composite.payment.repository;

import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderEntity.legacyOrderEntity;
import static com.ryuqq.setof.storage.legacy.payment.entity.QLegacyPaymentEntity.legacyPaymentEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.payment.dto.query.LegacyPaymentSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.payment.condition.LegacyWebPaymentCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.payment.dto.LegacyWebPaymentQueryDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebPaymentCompositeQueryDslRepository - 레거시 웹 결제 Composite 조회 Repository.
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
public class LegacyWebPaymentCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebPaymentCompositeConditionBuilder conditionBuilder;

    public LegacyWebPaymentCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebPaymentCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 결제 ID 목록 조회 (커서 기반 페이징용).
     *
     * <p>1단계: 결제 ID만 조회하여 페이징 처리.
     *
     * @param condition 검색 조건
     * @param limit 조회 개수
     * @return 결제 ID 목록
     */
    public List<Long> fetchPaymentIds(LegacyPaymentSearchCondition condition, int limit) {
        return queryFactory
                .select(legacyPaymentEntity.id)
                .from(legacyPaymentEntity)
                .innerJoin(legacyOrderEntity)
                .on(legacyPaymentEntity.id.eq(legacyOrderEntity.paymentId))
                .where(
                        conditionBuilder.userIdEq(condition.userId()),
                        conditionBuilder.betweenTime(condition.startDate(), condition.endDate()),
                        conditionBuilder.paymentIdLt(condition.lastDomainId()),
                        conditionBuilder.paymentFailNotIn(),
                        conditionBuilder.orderStatusIn(condition.orderStatusList()))
                .orderBy(legacyPaymentEntity.id.desc())
                .limit(limit)
                .fetch();
    }

    /**
     * 결제 목록 조회.
     *
     * <p>2단계: 결제 ID 목록으로 상세 정보 조회.
     *
     * <p>NOTE: 실제 구현에서는 payment_bill, payment_method 등과 조인하여 상세 정보를 조회해야 합니다. 현재는 기본 payment 테이블만
     * 조회합니다.
     *
     * @param paymentIds 결제 ID 목록
     * @return 결제 목록
     */
    public List<LegacyWebPaymentQueryDto> fetchPayments(List<Long> paymentIds) {
        if (paymentIds == null || paymentIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .select(createProjection())
                .from(legacyPaymentEntity)
                .where(conditionBuilder.paymentIdIn(paymentIds))
                .orderBy(legacyPaymentEntity.id.desc())
                .fetch();
    }

    /**
     * 결제 단건 조회 (ID).
     *
     * @param paymentId 결제 ID
     * @param userId 사용자 ID (보안 검증)
     * @return 결제 Optional
     */
    public LegacyWebPaymentQueryDto fetchPaymentById(long paymentId, long userId) {
        return queryFactory
                .select(createProjection())
                .from(legacyPaymentEntity)
                .where(conditionBuilder.paymentIdEq(paymentId), conditionBuilder.userIdEq(userId))
                .fetchOne();
    }

    /**
     * 결제 개수 조회.
     *
     * @param condition 검색 조건
     * @return 결제 개수
     */
    public long countPayments(LegacyPaymentSearchCondition condition) {
        Long count =
                queryFactory
                        .select(legacyPaymentEntity.count())
                        .from(legacyPaymentEntity)
                        .innerJoin(legacyOrderEntity)
                        .on(legacyPaymentEntity.id.eq(legacyOrderEntity.paymentId))
                        .where(
                                conditionBuilder.userIdEq(condition.userId()),
                                conditionBuilder.betweenTime(
                                        condition.startDate(), condition.endDate()),
                                conditionBuilder.paymentIdLt(condition.lastDomainId()),
                                conditionBuilder.paymentFailNotIn(),
                                conditionBuilder.orderStatusIn(condition.orderStatusList()))
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * Projections.constructor()로 Projection 생성.
     *
     * <p>@QueryProjection 대신 사용.
     *
     * <p>NOTE: 실제 구현에서는 payment_bill, payment_method와 조인하여 paymentAmount, usedMileageAmount,
     * paymentAgencyId, paymentMethodEnum 등을 조회해야 합니다. 현재는 기본 필드만 매핑합니다.
     */
    private com.querydsl.core.types.ConstructorExpression<LegacyWebPaymentQueryDto>
            createProjection() {
        return Projections.constructor(
                LegacyWebPaymentQueryDto.class,
                legacyPaymentEntity.id,
                legacyPaymentEntity.userId,
                legacyPaymentEntity.paymentStatus.stringValue(),
                // TODO: payment_bill과 조인하여 paymentAmount 조회
                Expressions.asNumber(0L), // paymentAmount (임시)
                // TODO: payment_snap_shot_mileage와 조인하여 usedMileageAmount 조회
                Expressions.asNumber(0.0), // usedMileageAmount (임시)
                // TODO: payment_bill과 조인하여 paymentAgencyId 조회
                Expressions.asString(""), // paymentAgencyId (임시)
                // TODO: payment_method와 조인하여 paymentMethodEnum 조회
                Expressions.asString(""), // paymentMethodEnum (임시)
                legacyPaymentEntity.paymentDate,
                legacyPaymentEntity.canceledDate,
                legacyPaymentEntity.siteName);
    }
}
