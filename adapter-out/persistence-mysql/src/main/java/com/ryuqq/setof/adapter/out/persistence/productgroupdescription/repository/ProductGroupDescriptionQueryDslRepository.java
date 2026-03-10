package com.ryuqq.setof.adapter.out.persistence.productgroupdescription.repository;

import static com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.QProductGroupDescriptionJpaEntity.productGroupDescriptionJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.condition.ProductGroupDescriptionConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.ProductGroupDescriptionJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ProductGroupDescriptionQueryDslRepository - 상품그룹 상세설명 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Repository
public class ProductGroupDescriptionQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final ProductGroupDescriptionConditionBuilder conditionBuilder;

    public ProductGroupDescriptionQueryDslRepository(
            JPAQueryFactory queryFactory,
            ProductGroupDescriptionConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 상품그룹 ID로 상세설명 조회.
     *
     * @param productGroupId 상품그룹 ID
     * @return 상세설명 Optional
     */
    public Optional<ProductGroupDescriptionJpaEntity> findByProductGroupId(Long productGroupId) {
        ProductGroupDescriptionJpaEntity entity =
                queryFactory
                        .selectFrom(productGroupDescriptionJpaEntity)
                        .where(conditionBuilder.productGroupIdEq(productGroupId))
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 상품그룹 ID 목록으로 상세설명 목록 조회.
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return 상세설명 목록
     */
    public List<ProductGroupDescriptionJpaEntity> findByProductGroupIds(
            List<Long> productGroupIds) {
        return queryFactory
                .selectFrom(productGroupDescriptionJpaEntity)
                .where(conditionBuilder.productGroupIdIn(productGroupIds))
                .fetch();
    }
}
