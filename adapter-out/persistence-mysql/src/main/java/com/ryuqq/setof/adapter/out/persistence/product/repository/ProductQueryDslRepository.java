package com.ryuqq.setof.adapter.out.persistence.product.repository;

import static com.ryuqq.setof.adapter.out.persistence.product.entity.QProductJpaEntity.productJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.product.entity.QProductOptionMappingJpaEntity.productOptionMappingJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.product.condition.ProductConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductOptionMappingJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ProductQueryDslRepository - 상품 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Repository
public class ProductQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final ProductConditionBuilder conditionBuilder;

    public ProductQueryDslRepository(
            JPAQueryFactory queryFactory, ProductConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * ID로 상품 조회 (삭제 상태 제외).
     *
     * @param id 상품 ID
     * @return 상품 Optional
     */
    public Optional<ProductJpaEntity> findById(Long id) {
        ProductJpaEntity entity =
                queryFactory
                        .selectFrom(productJpaEntity)
                        .where(conditionBuilder.idEq(id), conditionBuilder.statusNotDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * productGroupId로 상품 목록 조회.
     *
     * @param productGroupId 상품 그룹 ID
     * @return 상품 목록
     */
    public List<ProductJpaEntity> findByProductGroupId(Long productGroupId) {
        return queryFactory
                .selectFrom(productJpaEntity)
                .where(conditionBuilder.productGroupIdEq(productGroupId))
                .fetch();
    }

    /**
     * productGroupId와 ID 목록으로 상품 목록 조회.
     *
     * @param productGroupId 상품 그룹 ID
     * @param productIds 상품 ID 목록
     * @return 상품 목록
     */
    public List<ProductJpaEntity> findByProductGroupIdAndIdIn(
            Long productGroupId, List<Long> productIds) {
        return queryFactory
                .selectFrom(productJpaEntity)
                .where(
                        conditionBuilder.productGroupIdEq(productGroupId),
                        conditionBuilder.idIn(productIds))
                .fetch();
    }

    /**
     * ID 목록으로 상품 목록 조회.
     *
     * @param ids 상품 ID 목록
     * @return 상품 목록
     */
    public List<ProductJpaEntity> findByIdIn(List<Long> ids) {
        return queryFactory.selectFrom(productJpaEntity).where(conditionBuilder.idIn(ids)).fetch();
    }

    /**
     * productGroupId 목록으로 상품 목록 조회.
     *
     * @param productGroupIds 상품 그룹 ID 목록
     * @return 상품 목록
     */
    public List<ProductJpaEntity> findByProductGroupIdIn(List<Long> productGroupIds) {
        return queryFactory
                .selectFrom(productJpaEntity)
                .where(conditionBuilder.productGroupIdIn(productGroupIds))
                .fetch();
    }

    /**
     * productId로 옵션 매핑 목록 조회 (삭제되지 않은 것만).
     *
     * @param productId 상품 ID
     * @return 옵션 매핑 목록
     */
    public List<ProductOptionMappingJpaEntity> findOptionMappingsByProductId(Long productId) {
        return queryFactory
                .selectFrom(productOptionMappingJpaEntity)
                .where(
                        productOptionMappingJpaEntity.productId.eq(productId),
                        productOptionMappingJpaEntity.deleted.isFalse())
                .fetch();
    }

    /**
     * productId 목록으로 옵션 매핑 목록 조회.
     *
     * @param productIds 상품 ID 목록
     * @return 옵션 매핑 목록
     */
    public List<ProductOptionMappingJpaEntity> findOptionMappingsByProductIds(
            List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .selectFrom(productOptionMappingJpaEntity)
                .where(productOptionMappingJpaEntity.productId.in(productIds))
                .fetch();
    }
}
