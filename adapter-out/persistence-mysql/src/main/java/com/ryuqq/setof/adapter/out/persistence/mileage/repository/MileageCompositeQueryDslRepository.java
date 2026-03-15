package com.ryuqq.setof.adapter.out.persistence.mileage.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.mileage.condition.MileageCompositeConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.mileage.entity.QMileageEntryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.mileage.entity.QMileageLedgerJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.mileage.entity.QMileageTransactionJpaEntity;
import com.ryuqq.setof.application.mileage.dto.response.MileageHistoryItemResult;
import com.ryuqq.setof.domain.mileage.query.MileageHistorySearchCriteria;
import com.ryuqq.setof.domain.mileage.vo.MileageSummary;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * MileageCompositeQueryDslRepository - 마일리지 Composite QueryDSL Repository.
 *
 * <p>크로스 도메인 JOIN을 통한 마일리지 조회 구현.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class MileageCompositeQueryDslRepository {

    private static final QMileageLedgerJpaEntity ledger =
            QMileageLedgerJpaEntity.mileageLedgerJpaEntity;
    private static final QMileageEntryJpaEntity entry =
            QMileageEntryJpaEntity.mileageEntryJpaEntity;
    private static final QMileageTransactionJpaEntity transaction =
            QMileageTransactionJpaEntity.mileageTransactionJpaEntity;

    private static final ZoneId ZONE_KST = ZoneId.of("Asia/Seoul");

    private final JPAQueryFactory queryFactory;
    private final MileageCompositeConditionBuilder conditionBuilder;

    public MileageCompositeQueryDslRepository(
            JPAQueryFactory queryFactory, MileageCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 사용자 마일리지 요약 조회.
     *
     * <p>현재 사용 가능 마일리지 = ACTIVE 상태 entry들의 (earned - used) 합계. 소멸 예정 = 30일 내 만료되는 ACTIVE entry의 잔액
     * 합계. 적립 예정 = 현재는 0 (구매확정 미처리).
     */
    public MileageSummary fetchMileageSummary(long userId) {
        LocalDateTime now = LocalDateTime.now(ZONE_KST);
        LocalDateTime thirtyDaysLater = now.plusDays(30);

        // 현재 사용 가능 마일리지
        Double currentMileage =
                queryFactory
                        .select(
                                entry.earnedAmount
                                        .subtract(entry.usedAmount)
                                        .sum()
                                        .castToNum(Double.class))
                        .from(ledger)
                        .join(entry)
                        .on(
                                ledger.id.eq(entry.mileageLedgerId),
                                conditionBuilder.entryStatusActive())
                        .where(conditionBuilder.memberIdEq(userId))
                        .fetchOne();

        // 30일 내 소멸 예정 마일리지 (ACTIVE이면서 만료일이 현재~30일 이내)
        Double expectedExpireMileage =
                queryFactory
                        .select(
                                entry.earnedAmount
                                        .subtract(entry.usedAmount)
                                        .sum()
                                        .castToNum(Double.class))
                        .from(ledger)
                        .join(entry)
                        .on(
                                ledger.id.eq(entry.mileageLedgerId),
                                conditionBuilder.entryStatusActive(),
                                conditionBuilder.entryExpirationAfter(now),
                                conditionBuilder.entryExpirationBefore(thirtyDaysLater))
                        .where(conditionBuilder.memberIdEq(userId))
                        .fetchOne();

        double current = currentMileage != null ? currentMileage : 0.0;
        double expectedExpire = expectedExpireMileage != null ? expectedExpireMileage : 0.0;

        return MileageSummary.of(current, 0.0, expectedExpire);
    }

    /**
     * 마일리지 이력 목록 조회.
     *
     * <p>거래 이력을 엔트리, 원장과 JOIN하여 최신순으로 조회.
     */
    public List<MileageHistoryItemResult> fetchMileageHistories(
            MileageHistorySearchCriteria criteria) {

        List<Tuple> rows =
                queryFactory
                        .select(
                                transaction.id,
                                entry.id,
                                entry.title,
                                transaction.relatedPaymentId,
                                transaction.relatedOrderId,
                                transaction.changeAmount,
                                transaction.reason,
                                transaction.createdAt,
                                entry.expirationDate)
                        .from(transaction)
                        .join(entry)
                        .on(transaction.mileageEntryId.eq(entry.id))
                        .join(ledger)
                        .on(entry.mileageLedgerId.eq(ledger.id))
                        .where(
                                conditionBuilder.memberIdEq(criteria.userId()),
                                conditionBuilder.transactionReasonIn(criteria.reasons()))
                        .orderBy(transaction.createdAt.desc())
                        .offset(criteria.offset())
                        .limit(criteria.size())
                        .fetch();

        return rows.stream().map(this::toHistoryItemResult).toList();
    }

    /** 마일리지 이력 카운트 조회. */
    public long countMileageHistories(MileageHistorySearchCriteria criteria) {
        Long count =
                queryFactory
                        .select(transaction.count())
                        .from(transaction)
                        .join(entry)
                        .on(transaction.mileageEntryId.eq(entry.id))
                        .join(ledger)
                        .on(entry.mileageLedgerId.eq(ledger.id))
                        .where(
                                conditionBuilder.memberIdEq(criteria.userId()),
                                conditionBuilder.transactionReasonIn(criteria.reasons()))
                        .fetchOne();
        return count != null ? count : 0L;
    }

    // ── Private 헬퍼 ──

    private MileageHistoryItemResult toHistoryItemResult(Tuple row) {
        Instant createdAt = row.get(transaction.createdAt);
        LocalDateTime usedDate =
                createdAt != null ? LocalDateTime.ofInstant(createdAt, ZONE_KST) : null;

        return new MileageHistoryItemResult(
                row.get(transaction.id),
                row.get(entry.id),
                row.get(entry.title),
                row.get(transaction.relatedPaymentId),
                row.get(transaction.relatedOrderId),
                safeDouble(row.get(transaction.changeAmount)),
                row.get(transaction.reason),
                usedDate,
                row.get(entry.expirationDate));
    }

    private static double safeDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        return 0.0;
    }
}
