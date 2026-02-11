package com.ryuqq.setof.storage.legacy.composite.web.user.repository;

import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyRefundAccountEntity.legacyRefundAccountEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.user.dto.query.LegacyRefundAccountSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.user.condition.LegacyWebRefundAccountConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.user.dto.LegacyWebRefundAccountQueryDto;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * 레거시 환불 계좌 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebRefundAccountQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebRefundAccountConditionBuilder conditionBuilder;

    public LegacyWebRefundAccountQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebRefundAccountConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 사용자의 환불 계좌 조회.
     *
     * @param condition 검색 조건
     * @return 환불 계좌 Optional
     */
    public Optional<LegacyWebRefundAccountQueryDto> fetchRefundAccount(
            LegacyRefundAccountSearchCondition condition) {
        LegacyWebRefundAccountQueryDto dto =
                queryFactory
                        .select(
                                Projections.constructor(
                                        LegacyWebRefundAccountQueryDto.class,
                                        legacyRefundAccountEntity.id,
                                        legacyRefundAccountEntity.bankName,
                                        legacyRefundAccountEntity.accountNumber,
                                        legacyRefundAccountEntity.accountHolderName))
                        .from(legacyRefundAccountEntity)
                        .where(
                                conditionBuilder.userIdEq(condition.userId()),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return Optional.ofNullable(dto);
    }
}
