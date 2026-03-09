package com.ryuqq.setof.storage.legacy.payment.repository;

import static com.ryuqq.setof.storage.legacy.payment.entity.QLegacyPaymentBillEntity.legacyPaymentBillEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyPaymentBillQueryDslRepository - 레거시 결제 청구서 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyPaymentBillQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public LegacyPaymentBillQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 결제 ID로 PG사 거래 ID 조회.
     *
     * @param paymentId 결제 ID
     * @return PG사 거래 ID (PAYMENT_AGENCY_ID). 없으면 empty.
     */
    public Optional<String> findPaymentAgencyIdByPaymentId(long paymentId) {
        String result =
                queryFactory
                        .select(legacyPaymentBillEntity.paymentAgencyId)
                        .from(legacyPaymentBillEntity)
                        .where(legacyPaymentBillEntity.paymentId.eq(paymentId))
                        .fetchFirst();
        return Optional.ofNullable(result);
    }
}
