package com.ryuqq.setof.storage.legacy.paymentmethod.repository;

import static com.ryuqq.setof.storage.legacy.commoncode.entity.QLegacyCommonCodeEntity.legacyCommonCodeEntity;
import static com.ryuqq.setof.storage.legacy.paymentmethod.entity.QLegacyPaymentMethodEntity.legacyPaymentMethodEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.paymentmethod.dto.LegacyPaymentMethodQueryDto;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * LegacyPaymentMethodQueryDslRepository - 레거시 결제 수단 QueryDSL 레포지토리.
 *
 * <p>payment_method + common_code(GROUP_ID=17) JOIN으로 display name 포함 조회.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyPaymentMethodQueryDslRepository {

    private static final long PAYMENT_METHOD_CODE_GROUP_ID = 17L;

    private final JPAQueryFactory queryFactory;

    public LegacyPaymentMethodQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 활성화된 결제 수단 목록 조회 (display name 포함).
     *
     * @return 결제 수단 DTO 목록
     */
    public List<LegacyPaymentMethodQueryDto> findActivePaymentMethods() {
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyPaymentMethodQueryDto.class,
                                legacyPaymentMethodEntity.paymentMethod,
                                legacyCommonCodeEntity.codeDetailDisplayName,
                                legacyPaymentMethodEntity.paymentMethodMerchantKey))
                .from(legacyPaymentMethodEntity)
                .innerJoin(legacyCommonCodeEntity)
                .on(
                        legacyCommonCodeEntity.codeGroupId.eq(PAYMENT_METHOD_CODE_GROUP_ID),
                        legacyCommonCodeEntity.displayOrder.eq(
                                legacyPaymentMethodEntity.id.intValue()))
                .where(
                        legacyPaymentMethodEntity.displayYn.eq(
                                com.ryuqq.setof.storage.legacy.common.Yn.Y),
                        legacyPaymentMethodEntity.deleteYn.eq(
                                com.ryuqq.setof.storage.legacy.common.Yn.N))
                .orderBy(legacyPaymentMethodEntity.id.asc())
                .fetch();
    }
}
