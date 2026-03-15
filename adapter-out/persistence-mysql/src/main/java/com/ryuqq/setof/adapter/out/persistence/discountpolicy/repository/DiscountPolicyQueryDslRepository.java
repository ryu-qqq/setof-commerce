package com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository;

import static com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.QDiscountPolicyJpaEntity.discountPolicyJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.QDiscountTargetJpaEntity.discountTargetJpaEntity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.condition.DiscountPolicyConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountTargetJpaEntity;
import com.ryuqq.setof.domain.discount.query.DiscountPolicySortKey;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * DiscountPolicyQueryDslRepository - 할인 정책 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: QueryDslRepository는 JPAQueryFactory만 사용.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class DiscountPolicyQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final DiscountPolicyConditionBuilder conditionBuilder;

    public DiscountPolicyQueryDslRepository(
            JPAQueryFactory queryFactory, DiscountPolicyConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * ID로 할인 정책 조회.
     *
     * @param id 할인 정책 ID
     * @return Optional entity
     */
    public Optional<DiscountPolicyJpaEntity> findById(long id) {
        DiscountPolicyJpaEntity entity =
                queryFactory
                        .selectFrom(discountPolicyJpaEntity)
                        .where(discountPolicyJpaEntity.id.eq(id))
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 특정 타겟에 적용 가능한 활성 할인 정책 ID 목록 조회.
     *
     * <p>discount_target 테이블에서 타겟 매칭 → discount_policy 조건 필터링.
     *
     * @param targetType 대상 유형 (BRAND, SELLER, CATEGORY, PRODUCT)
     * @param targetId 대상 ID
     * @param now 현재 시각 (기간 필터용)
     * @return 적용 가능한 정책 ID 목록
     */
    public List<Long> findActivePolicyIdsByTarget(
            DiscountTargetJpaEntity.TargetType targetType, long targetId, Instant now) {
        return queryFactory
                .select(discountPolicyJpaEntity.id)
                .from(discountTargetJpaEntity)
                .join(discountPolicyJpaEntity)
                .on(discountTargetJpaEntity.discountPolicyId.eq(discountPolicyJpaEntity.id))
                .where(
                        discountTargetJpaEntity.targetType.eq(targetType),
                        discountTargetJpaEntity.targetId.eq(targetId),
                        discountTargetJpaEntity.active.isTrue(),
                        discountPolicyJpaEntity.active.isTrue(),
                        discountPolicyJpaEntity.deletedAt.isNull(),
                        discountPolicyJpaEntity.startAt.loe(now),
                        discountPolicyJpaEntity.endAt.gt(now))
                .orderBy(discountPolicyJpaEntity.priority.desc())
                .fetch();
    }

    /**
     * ID 목록으로 할인 정책 조회.
     *
     * @param ids 정책 ID 목록
     * @return entity 목록
     */
    public List<DiscountPolicyJpaEntity> findAllByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .selectFrom(discountPolicyJpaEntity)
                .where(discountPolicyJpaEntity.id.in(ids))
                .fetch();
    }

    /**
     * 특정 정책의 타겟 목록 조회.
     *
     * @param discountPolicyId 정책 ID
     * @return 타겟 entity 목록
     */
    public List<DiscountTargetJpaEntity> findTargetsByPolicyId(long discountPolicyId) {
        return queryFactory
                .selectFrom(discountTargetJpaEntity)
                .where(discountTargetJpaEntity.discountPolicyId.eq(discountPolicyId))
                .fetch();
    }

    /**
     * 여러 정책의 타겟 목록 일괄 조회.
     *
     * @param policyIds 정책 ID 목록
     * @return 타겟 entity 목록
     */
    public List<DiscountTargetJpaEntity> findTargetsByPolicyIds(List<Long> policyIds) {
        if (policyIds == null || policyIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .selectFrom(discountTargetJpaEntity)
                .where(discountTargetJpaEntity.discountPolicyId.in(policyIds))
                .fetch();
    }

    /**
     * 검색 조건으로 할인 정책 목록 조회 (페이징).
     *
     * @param applicationType 적용 방식 필터
     * @param publisherType 발행 주체 필터
     * @param stackingGroup 스태킹 그룹 필터
     * @param sellerId 셀러 ID 필터
     * @param activeOnly 활성 전용 여부
     * @param sortKey 정렬 키
     * @param ascending 오름차순 여부
     * @param offset 오프셋
     * @param limit 페이지 크기
     * @return entity 목록
     */
    public List<DiscountPolicyJpaEntity> findByCriteria(
            DiscountPolicyJpaEntity.ApplicationType applicationType,
            DiscountPolicyJpaEntity.PublisherType publisherType,
            DiscountPolicyJpaEntity.StackingGroup stackingGroup,
            Long sellerId,
            boolean activeOnly,
            DiscountPolicySortKey sortKey,
            boolean ascending,
            long offset,
            int limit) {
        return queryFactory
                .selectFrom(discountPolicyJpaEntity)
                .where(
                        conditionBuilder.applicationTypeEq(applicationType),
                        conditionBuilder.publisherTypeEq(publisherType),
                        conditionBuilder.stackingGroupEq(stackingGroup),
                        conditionBuilder.sellerIdEq(sellerId),
                        activeOnly ? conditionBuilder.activeEq(true) : null,
                        conditionBuilder.notDeleted())
                .orderBy(toOrderSpecifier(sortKey, ascending))
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    /**
     * 검색 조건으로 할인 정책 총 건수 조회.
     *
     * @param applicationType 적용 방식 필터
     * @param publisherType 발행 주체 필터
     * @param stackingGroup 스태킹 그룹 필터
     * @param sellerId 셀러 ID 필터
     * @param activeOnly 활성 전용 여부
     * @return 총 건수
     */
    public long countByCriteria(
            DiscountPolicyJpaEntity.ApplicationType applicationType,
            DiscountPolicyJpaEntity.PublisherType publisherType,
            DiscountPolicyJpaEntity.StackingGroup stackingGroup,
            Long sellerId,
            boolean activeOnly) {
        Long count =
                queryFactory
                        .select(discountPolicyJpaEntity.count())
                        .from(discountPolicyJpaEntity)
                        .where(
                                conditionBuilder.applicationTypeEq(applicationType),
                                conditionBuilder.publisherTypeEq(publisherType),
                                conditionBuilder.stackingGroupEq(stackingGroup),
                                conditionBuilder.sellerIdEq(sellerId),
                                activeOnly ? conditionBuilder.activeEq(true) : null,
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    private OrderSpecifier<?> toOrderSpecifier(DiscountPolicySortKey sortKey, boolean ascending) {
        return switch (sortKey) {
            case CREATED_AT ->
                    ascending
                            ? discountPolicyJpaEntity.createdAt.asc()
                            : discountPolicyJpaEntity.createdAt.desc();
            case NAME ->
                    ascending
                            ? discountPolicyJpaEntity.name.asc()
                            : discountPolicyJpaEntity.name.desc();
            case PRIORITY ->
                    ascending
                            ? discountPolicyJpaEntity.priority.asc()
                            : discountPolicyJpaEntity.priority.desc();
            case START_AT ->
                    ascending
                            ? discountPolicyJpaEntity.startAt.asc()
                            : discountPolicyJpaEntity.startAt.desc();
        };
    }
}
