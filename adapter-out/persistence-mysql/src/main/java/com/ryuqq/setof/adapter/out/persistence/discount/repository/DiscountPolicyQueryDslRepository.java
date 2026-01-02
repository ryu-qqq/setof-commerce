package com.ryuqq.setof.adapter.out.persistence.discount.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity.DiscountGroupType;
import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity.DiscountTargetTypeEnum;
import com.ryuqq.setof.adapter.out.persistence.discount.entity.QDiscountPolicyJpaEntity;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * DiscountPolicyQueryDslRepository - DiscountPolicy QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class DiscountPolicyQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QDiscountPolicyJpaEntity qPolicy =
            QDiscountPolicyJpaEntity.discountPolicyJpaEntity;

    public DiscountPolicyQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /** ID로 DiscountPolicy 단건 조회 */
    public Optional<DiscountPolicyJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qPolicy)
                        .where(qPolicy.id.eq(id), qPolicy.deletedAt.isNull())
                        .fetchOne());
    }

    /** ID로 DiscountPolicy 단건 조회 (삭제 포함) */
    public Optional<DiscountPolicyJpaEntity> findByIdIncludeDeleted(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qPolicy).where(qPolicy.id.eq(id)).fetchOne());
    }

    /** 셀러 ID로 DiscountPolicy 목록 조회 */
    public List<DiscountPolicyJpaEntity> findBySellerId(Long sellerId, boolean includeDeleted) {
        var query = queryFactory.selectFrom(qPolicy).where(qPolicy.sellerId.eq(sellerId));

        if (!includeDeleted) {
            query.where(qPolicy.deletedAt.isNull());
        }

        return query.orderBy(qPolicy.priority.asc(), qPolicy.createdAt.desc()).fetch();
    }

    /** 셀러 ID와 그룹으로 DiscountPolicy 목록 조회 */
    public List<DiscountPolicyJpaEntity> findBySellerIdAndGroup(
            Long sellerId, DiscountGroupType discountGroup, boolean activeOnly) {
        var query =
                queryFactory
                        .selectFrom(qPolicy)
                        .where(
                                qPolicy.sellerId.eq(sellerId),
                                qPolicy.discountGroup.eq(discountGroup),
                                qPolicy.deletedAt.isNull());

        if (activeOnly) {
            query.where(qPolicy.isActive.isTrue());
        }

        return query.orderBy(qPolicy.priority.asc()).fetch();
    }

    /** 적용 대상 타입과 ID로 DiscountPolicy 목록 조회 */
    public List<DiscountPolicyJpaEntity> findByTargetTypeAndTargetId(
            DiscountTargetTypeEnum targetType, Long targetId, boolean activeOnly) {
        var query =
                queryFactory
                        .selectFrom(qPolicy)
                        .where(
                                qPolicy.targetType.eq(targetType),
                                qPolicy.targetIds.contains(String.valueOf(targetId)),
                                qPolicy.deletedAt.isNull());

        if (activeOnly) {
            query.where(qPolicy.isActive.isTrue());
        }

        return query.orderBy(qPolicy.priority.asc()).fetch();
    }

    /** 현재 유효한 할인 정책 목록 조회 */
    public List<DiscountPolicyJpaEntity> findValidPolicies(Long sellerId) {
        Instant now = Instant.now();
        return queryFactory
                .selectFrom(qPolicy)
                .where(
                        qPolicy.sellerId.eq(sellerId),
                        qPolicy.isActive.isTrue(),
                        qPolicy.deletedAt.isNull(),
                        qPolicy.validStartAt.loe(now),
                        qPolicy.validEndAt.goe(now))
                .orderBy(qPolicy.priority.asc())
                .fetch();
    }

    /** 셀러의 정책 개수 조회 */
    public long countBySellerId(Long sellerId, boolean includeDeleted) {
        var query =
                queryFactory
                        .select(qPolicy.count())
                        .from(qPolicy)
                        .where(qPolicy.sellerId.eq(sellerId));

        if (!includeDeleted) {
            query.where(qPolicy.deletedAt.isNull());
        }

        Long count = query.fetchOne();
        return count != null ? count : 0L;
    }

    /** ID로 존재 여부 확인 */
    public boolean existsById(Long id) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qPolicy)
                        .where(qPolicy.id.eq(id), qPolicy.deletedAt.isNull())
                        .fetchFirst();
        return count != null;
    }
}
