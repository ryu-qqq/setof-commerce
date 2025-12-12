package com.ryuqq.setof.adapter.out.persistence.refundpolicy.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.refundpolicy.entity.QRefundPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.refundpolicy.entity.RefundPolicyJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * RefundPolicyQueryDslRepository - RefundPolicy QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class RefundPolicyQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QRefundPolicyJpaEntity qPolicy =
            QRefundPolicyJpaEntity.refundPolicyJpaEntity;

    public RefundPolicyQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /** ID로 RefundPolicy 단건 조회 */
    public Optional<RefundPolicyJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qPolicy)
                        .where(qPolicy.id.eq(id), qPolicy.deletedAt.isNull())
                        .fetchOne());
    }

    /** ID로 RefundPolicy 단건 조회 (삭제 포함) */
    public Optional<RefundPolicyJpaEntity> findByIdIncludeDeleted(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qPolicy).where(qPolicy.id.eq(id)).fetchOne());
    }

    /** 셀러 ID로 RefundPolicy 목록 조회 */
    public List<RefundPolicyJpaEntity> findBySellerId(Long sellerId, boolean includeDeleted) {
        var query = queryFactory.selectFrom(qPolicy).where(qPolicy.sellerId.eq(sellerId));

        if (!includeDeleted) {
            query.where(qPolicy.deletedAt.isNull());
        }

        return query.orderBy(qPolicy.displayOrder.asc()).fetch();
    }

    /** 셀러의 기본 정책 조회 */
    public Optional<RefundPolicyJpaEntity> findDefaultBySellerId(Long sellerId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qPolicy)
                        .where(
                                qPolicy.sellerId.eq(sellerId),
                                qPolicy.isDefault.isTrue(),
                                qPolicy.deletedAt.isNull())
                        .fetchOne());
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
