package com.ryuqq.setof.storage.legacy.refundaccount.repository;

import static com.ryuqq.setof.storage.legacy.refundaccount.entity.QLegacyRefundAccountEntity.legacyRefundAccountEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.refundaccount.condition.LegacyRefundAccountConditionBuilder;
import com.ryuqq.setof.storage.legacy.refundaccount.entity.LegacyRefundAccountEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyRefundAccountQueryDslRepository - 레거시 환불 계좌 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>단일 테이블 조회이므로 Projections.constructor 대신 엔티티 직접 조회를 사용합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyRefundAccountQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyRefundAccountConditionBuilder conditionBuilder;

    public LegacyRefundAccountQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyRefundAccountConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * userId로 환불 계좌를 조회합니다 (deleteYn = 'N' 필터 적용).
     *
     * <p>GET 조회 엔드포인트에서 사용합니다. 소프트 딜리트된 계좌는 조회되지 않습니다.
     *
     * @param userId 사용자 ID
     * @return 환불 계좌 엔티티 (Optional)
     */
    public Optional<LegacyRefundAccountEntity> findActiveByUserId(long userId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(legacyRefundAccountEntity)
                        .where(conditionBuilder.userIdEq(userId), conditionBuilder.notDeleted())
                        .fetchOne());
    }

    /**
     * userId + refundAccountId로 엔티티를 조회합니다 (deleteYn 필터 없음).
     *
     * <p>PUT/DELETE 엔드포인트에서 소유권 검증 후 엔티티를 취득할 때 사용합니다. deleteYn 조건 없이 조회하는 것은 레거시 설계 의도를 그대로 보존합니다.
     *
     * @param userId 사용자 ID
     * @param refundAccountId 환불 계좌 ID
     * @return 환불 계좌 엔티티 (Optional)
     */
    public Optional<LegacyRefundAccountEntity> findByUserIdAndId(
            long userId, long refundAccountId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(legacyRefundAccountEntity)
                        .where(
                                conditionBuilder.userIdEq(userId),
                                conditionBuilder.idEq(refundAccountId))
                        .fetchOne());
    }

    /**
     * userId로 환불 계좌 목록을 조회합니다 (deleteYn = 'N' 필터 적용).
     *
     * @param userId 사용자 ID
     * @return 환불 계좌 엔티티 목록
     */
    public List<LegacyRefundAccountEntity> findAllActiveByUserId(long userId) {
        return queryFactory
                .selectFrom(legacyRefundAccountEntity)
                .where(conditionBuilder.userIdEq(userId), conditionBuilder.notDeleted())
                .fetch();
    }
}
