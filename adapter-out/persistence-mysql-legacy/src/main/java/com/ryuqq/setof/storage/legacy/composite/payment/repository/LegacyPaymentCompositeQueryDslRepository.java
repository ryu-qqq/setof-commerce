package com.ryuqq.setof.storage.legacy.composite.payment.repository;

import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderEntity.legacyOrderEntity;
import static com.ryuqq.setof.storage.legacy.payment.entity.QLegacyPaymentBillEntity.legacyPaymentBillEntity;
import static com.ryuqq.setof.storage.legacy.payment.entity.QLegacyPaymentEntity.legacyPaymentEntity;
import static com.ryuqq.setof.storage.legacy.payment.entity.QLegacyVBankAccountEntity.legacyVBankAccountEntity;
import static com.ryuqq.setof.storage.legacy.paymentmethod.entity.QLegacyPaymentMethodEntity.legacyPaymentMethodEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.common.vo.DateRange;
import com.ryuqq.setof.domain.payment.query.PaymentSearchCriteria;
import com.ryuqq.setof.storage.legacy.composite.payment.condition.LegacyPaymentCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.payment.dto.LegacyPaymentOverviewFlatDto;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyPaymentCompositeQueryDslRepository - 결제 Composite 조회 Repository.
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
public class LegacyPaymentCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyPaymentCompositeConditionBuilder conditionBuilder;

    public LegacyPaymentCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyPaymentCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 검색 조건으로 결제 ID 목록 조회 (커서 기반 페이징).
     *
     * <p>payment INNER JOIN orders 구조로, userId·커서·날짜 범위·결제 상태 필터를 동적으로 적용합니다.
     *
     * @param criteria 결제 검색 조건
     * @return 결제 ID 목록 (fetchSize = size + 1)
     */
    public List<Long> fetchPaymentIds(PaymentSearchCriteria criteria) {
        DateRange dateRange = criteria.dateRange();
        LocalDateTime startDateTime =
                dateRange != null && dateRange.startDate() != null
                        ? dateRange.startDate().atStartOfDay()
                        : null;
        LocalDateTime endDateTime =
                dateRange != null && dateRange.endDate() != null
                        ? dateRange.endDate().plusDays(1).atStartOfDay().minusNanos(1)
                        : null;

        return queryFactory
                .select(legacyPaymentEntity.id)
                .from(legacyPaymentEntity)
                .innerJoin(legacyOrderEntity)
                .on(legacyOrderEntity.paymentId.eq(legacyPaymentEntity.id))
                .where(
                        conditionBuilder.userIdEq(criteria.userId()),
                        conditionBuilder.paymentIdLt(criteria.cursor()),
                        conditionBuilder.insertDateBetween(startDateTime, endDateTime),
                        conditionBuilder.paymentStatusNotFailed(),
                        conditionBuilder.orderStatusIn(criteria.orderStatuses()))
                .orderBy(legacyPaymentEntity.id.desc())
                .limit(criteria.fetchSize())
                .distinct()
                .fetch();
    }

    /**
     * 결제 ID 목록으로 결제 목록 개요 조회 (Composite JOIN).
     *
     * <p>payment INNER JOIN orders INNER JOIN payment_bill LEFT JOIN payment_method LEFT JOIN
     * vbank_account 구조로 조회합니다.
     *
     * @param paymentIds 결제 ID 목록
     * @return 결제 목록 개요 flat DTO 목록
     */
    public List<LegacyPaymentOverviewFlatDto> fetchPaymentOverviews(List<Long> paymentIds) {
        if (paymentIds == null || paymentIds.isEmpty()) {
            return List.of();
        }

        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyPaymentOverviewFlatDto.class,
                                // payment
                                legacyPaymentEntity.id,
                                legacyPaymentEntity.paymentStatus.stringValue(),
                                // payment_method
                                legacyPaymentMethodEntity.paymentMethod.coalesce(""),
                                // payment
                                legacyPaymentEntity.paymentDate,
                                legacyPaymentEntity.canceledDate,
                                // payment_bill
                                legacyPaymentBillEntity.paymentAmount,
                                legacyPaymentBillEntity.usedMileageAmount,
                                legacyPaymentBillEntity.paymentAgencyId.coalesce(""),
                                legacyPaymentBillEntity.cardName.coalesce(""),
                                legacyPaymentBillEntity.cardNumber.coalesce(""),
                                // payment (추가 필드)
                                legacyPaymentEntity.userId,
                                legacyPaymentEntity.siteName.coalesce(""),
                                // payment_bill (결제 수단 ID)
                                legacyPaymentBillEntity.paymentMethodId,
                                // orders
                                legacyOrderEntity.id,
                                legacyOrderEntity.orderStatus.stringValue(),
                                // vbank_account (LEFT JOIN)
                                legacyVBankAccountEntity.vBankName.coalesce(""),
                                legacyVBankAccountEntity.vBankNumber.coalesce(""),
                                legacyVBankAccountEntity.paymentAmount.coalesce(0L),
                                legacyVBankAccountEntity.vBankDueDate))
                .from(legacyPaymentEntity)
                .innerJoin(legacyOrderEntity)
                .on(legacyOrderEntity.paymentId.eq(legacyPaymentEntity.id))
                .innerJoin(legacyPaymentBillEntity)
                .on(legacyPaymentBillEntity.paymentId.eq(legacyPaymentEntity.id))
                .leftJoin(legacyPaymentMethodEntity)
                .on(legacyPaymentMethodEntity.id.eq(legacyPaymentBillEntity.paymentMethodId))
                .leftJoin(legacyVBankAccountEntity)
                .on(legacyVBankAccountEntity.paymentId.eq(legacyPaymentEntity.id))
                .where(conditionBuilder.paymentIdIn(paymentIds))
                .orderBy(legacyPaymentEntity.id.desc())
                .fetch();
    }

    /**
     * 검색 조건으로 결제 건수 조회 (totalElements 계산용).
     *
     * <p>fetchPaymentIds와 동일한 WHERE 조건을 사용하되 COUNT DISTINCT로 조회합니다.
     *
     * @param criteria 결제 검색 조건
     * @return 결제 건수
     */
    public long countPayments(PaymentSearchCriteria criteria) {
        DateRange dateRange = criteria.dateRange();
        LocalDateTime startDateTime =
                dateRange != null && dateRange.startDate() != null
                        ? dateRange.startDate().atStartOfDay()
                        : null;
        LocalDateTime endDateTime =
                dateRange != null && dateRange.endDate() != null
                        ? dateRange.endDate().plusDays(1).atStartOfDay().minusNanos(1)
                        : null;

        Long count =
                queryFactory
                        .select(legacyPaymentEntity.id.countDistinct())
                        .from(legacyPaymentEntity)
                        .innerJoin(legacyOrderEntity)
                        .on(legacyOrderEntity.paymentId.eq(legacyPaymentEntity.id))
                        .where(
                                conditionBuilder.userIdEq(criteria.userId()),
                                conditionBuilder.insertDateBetween(startDateTime, endDateTime),
                                conditionBuilder.paymentStatusNotFailed(),
                                conditionBuilder.orderStatusIn(criteria.orderStatuses()))
                        .fetchOne();
        return count != null ? count : 0L;
    }
}
