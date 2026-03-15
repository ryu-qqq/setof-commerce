package com.ryuqq.setof.adapter.out.persistence.refundaccount.repository;

import static com.ryuqq.setof.adapter.out.persistence.refundaccount.entity.QRefundAccountJpaEntity.refundAccountJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.refundaccount.condition.RefundAccountConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.refundaccount.entity.RefundAccountJpaEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * RefundAccountQueryDslRepository - 환불 계좌 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>NOTE: RefundAccountJpaEntity는 memberId(Long) 컬럼을 보유합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class RefundAccountQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final RefundAccountConditionBuilder conditionBuilder;

    public RefundAccountQueryDslRepository(
            JPAQueryFactory queryFactory, RefundAccountConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 회원 ID로 활성 환불 계좌 조회 (삭제되지 않은 것만).
     *
     * @param memberId 회원 ID
     * @return 환불 계좌 Optional
     */
    public Optional<RefundAccountJpaEntity> findByMemberId(Long memberId) {
        RefundAccountJpaEntity entity =
                queryFactory
                        .selectFrom(refundAccountJpaEntity)
                        .where(conditionBuilder.memberIdEq(memberId), conditionBuilder.notDeleted())
                        .fetchFirst();
        return Optional.ofNullable(entity);
    }

    /**
     * 회원 ID + 환불 계좌 ID로 환불 계좌 조회 (삭제 여부 무관).
     *
     * @param memberId 회원 ID
     * @param refundAccountId 환불 계좌 ID
     * @return 환불 계좌 Optional
     */
    public Optional<RefundAccountJpaEntity> findByMemberIdAndId(
            Long memberId, Long refundAccountId) {
        RefundAccountJpaEntity entity =
                queryFactory
                        .selectFrom(refundAccountJpaEntity)
                        .where(
                                conditionBuilder.memberIdEq(memberId),
                                conditionBuilder.idEq(refundAccountId))
                        .fetchOne();
        return Optional.ofNullable(entity);
    }
}
