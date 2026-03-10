package com.ryuqq.setof.adapter.out.persistence.productnotice.repository;

import static com.ryuqq.setof.adapter.out.persistence.productnotice.entity.QProductNoticeEntryJpaEntity.productNoticeEntryJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.productnotice.entity.QProductNoticeJpaEntity.productNoticeJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.productnotice.condition.ProductNoticeConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeEntryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ProductNoticeQueryDslRepository - 상품고시 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Repository
public class ProductNoticeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final ProductNoticeConditionBuilder conditionBuilder;

    public ProductNoticeQueryDslRepository(
            JPAQueryFactory queryFactory, ProductNoticeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 상품그룹 ID로 상품고시 조회.
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품고시 Optional
     */
    public Optional<ProductNoticeJpaEntity> findByProductGroupId(Long productGroupId) {
        ProductNoticeJpaEntity entity =
                queryFactory
                        .selectFrom(productNoticeJpaEntity)
                        .where(conditionBuilder.productGroupIdEq(productGroupId))
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 상품고시 ID로 고시 항목 목록 조회.
     *
     * @param productNoticeId 상품고시 ID
     * @return 고시 항목 목록
     */
    public List<ProductNoticeEntryJpaEntity> findEntriesByProductNoticeId(Long productNoticeId) {
        return queryFactory
                .selectFrom(productNoticeEntryJpaEntity)
                .where(productNoticeEntryJpaEntity.productNoticeId.eq(productNoticeId))
                .orderBy(productNoticeEntryJpaEntity.sortOrder.asc())
                .fetch();
    }

    /**
     * 상품고시 ID 목록으로 고시 항목 목록 조회.
     *
     * @param productNoticeIds 상품고시 ID 목록
     * @return 고시 항목 목록
     */
    public List<ProductNoticeEntryJpaEntity> findEntriesByProductNoticeIds(
            List<Long> productNoticeIds) {
        if (productNoticeIds == null || productNoticeIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .selectFrom(productNoticeEntryJpaEntity)
                .where(productNoticeEntryJpaEntity.productNoticeId.in(productNoticeIds))
                .orderBy(
                        productNoticeEntryJpaEntity.productNoticeId.asc(),
                        productNoticeEntryJpaEntity.sortOrder.asc())
                .fetch();
    }
}
