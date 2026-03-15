package com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository;

import static com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.QDiscountTargetJpaEntity.discountTargetJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.condition.DiscountTargetConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountTargetJpaEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * DiscountTargetQueryDslRepository - 할인 적용 대상 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: QueryDslRepository는 JPAQueryFactory만 사용.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class DiscountTargetQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final DiscountTargetConditionBuilder conditionBuilder;

    public DiscountTargetQueryDslRepository(
            JPAQueryFactory queryFactory, DiscountTargetConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 정책 ID로 타겟 목록 조회.
     *
     * @param discountPolicyId 정책 ID
     * @return 타겟 entity 목록
     */
    public List<DiscountTargetJpaEntity> findByPolicyId(long discountPolicyId) {
        return queryFactory
                .selectFrom(discountTargetJpaEntity)
                .where(conditionBuilder.policyIdEq(discountPolicyId))
                .fetch();
    }

    /**
     * 여러 정책의 타겟 목록 일괄 조회.
     *
     * @param policyIds 정책 ID 목록
     * @return 타겟 entity 목록
     */
    public List<DiscountTargetJpaEntity> findByPolicyIds(List<Long> policyIds) {
        if (policyIds == null || policyIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .selectFrom(discountTargetJpaEntity)
                .where(conditionBuilder.policyIdIn(policyIds))
                .fetch();
    }

    /**
     * 정책 ID별 활성 타겟 수 조회.
     *
     * @param discountPolicyId 정책 ID
     * @return 활성 타겟 수
     */
    public long countByPolicyId(long discountPolicyId) {
        Long count =
                queryFactory
                        .select(discountTargetJpaEntity.count())
                        .from(discountTargetJpaEntity)
                        .where(
                                conditionBuilder.policyIdEq(discountPolicyId),
                                conditionBuilder.activeEq(true))
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 대상 유형+ID로 활성 타겟 조회.
     *
     * @param targetType 대상 유형
     * @param targetId 대상 ID
     * @return 타겟 entity 목록
     */
    public List<DiscountTargetJpaEntity> findByTargetTypeAndTargetId(
            DiscountTargetJpaEntity.TargetType targetType, long targetId) {
        return queryFactory
                .selectFrom(discountTargetJpaEntity)
                .where(
                        conditionBuilder.targetTypeEq(targetType),
                        conditionBuilder.targetIdEq(targetId),
                        conditionBuilder.activeEq(true))
                .fetch();
    }
}
