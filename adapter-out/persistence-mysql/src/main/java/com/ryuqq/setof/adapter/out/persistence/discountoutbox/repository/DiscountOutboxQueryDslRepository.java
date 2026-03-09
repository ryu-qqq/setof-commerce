package com.ryuqq.setof.adapter.out.persistence.discountoutbox.repository;

import static com.ryuqq.setof.adapter.out.persistence.discountoutbox.entity.QDiscountOutboxJpaEntity.discountOutboxJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.discountoutbox.entity.DiscountOutboxJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountoutbox.entity.DiscountOutboxJpaEntity.Status;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * DiscountOutboxQueryDslRepository - 할인 아웃박스 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: QueryDslRepository는 JPAQueryFactory만 사용.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class DiscountOutboxQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public DiscountOutboxQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 조회.
     *
     * @param id 아웃박스 ID
     * @return Optional entity
     */
    public Optional<DiscountOutboxJpaEntity> findById(long id) {
        DiscountOutboxJpaEntity entity =
                queryFactory
                        .selectFrom(discountOutboxJpaEntity)
                        .where(discountOutboxJpaEntity.id.eq(id))
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 상태별 배치 조회.
     *
     * @param status 조회 상태
     * @param limit 최대 조회 건수
     * @return entity 목록
     */
    public List<DiscountOutboxJpaEntity> findByStatus(Status status, int limit) {
        return queryFactory
                .selectFrom(discountOutboxJpaEntity)
                .where(discountOutboxJpaEntity.status.eq(status))
                .orderBy(discountOutboxJpaEntity.createdAt.asc())
                .limit(limit)
                .fetch();
    }

    /**
     * Stuck 아웃박스 조회 (PUBLISHED 상태에서 timeoutBefore 이전에 업데이트된 건).
     *
     * @param timeoutBefore 타임아웃 기준 시각
     * @param limit 최대 조회 건수
     * @return entity 목록
     */
    public List<DiscountOutboxJpaEntity> findStuckPublished(Instant timeoutBefore, int limit) {
        return queryFactory
                .selectFrom(discountOutboxJpaEntity)
                .where(
                        discountOutboxJpaEntity.status.eq(Status.PUBLISHED),
                        discountOutboxJpaEntity.updatedAt.before(timeoutBefore))
                .orderBy(discountOutboxJpaEntity.updatedAt.asc())
                .limit(limit)
                .fetch();
    }

    /**
     * 특정 타겟 + 상태로 존재 여부 확인.
     *
     * @param targetType 타겟 유형
     * @param targetId 타겟 ID
     * @param status 상태
     * @return 존재 여부
     */
    public boolean existsByTargetAndStatus(String targetType, long targetId, Status status) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(discountOutboxJpaEntity)
                        .where(
                                discountOutboxJpaEntity.targetType.eq(targetType),
                                discountOutboxJpaEntity.targetId.eq(targetId),
                                discountOutboxJpaEntity.status.eq(status))
                        .fetchFirst();
        return count != null;
    }
}
