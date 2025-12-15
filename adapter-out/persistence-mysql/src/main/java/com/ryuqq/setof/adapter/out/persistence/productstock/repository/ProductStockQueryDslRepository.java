package com.ryuqq.setof.adapter.out.persistence.productstock.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.productstock.entity.ProductStockJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productstock.entity.QProductStockJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ProductStockQueryDslRepository - ProductStock QueryDSL Repository
 *
 * <p>QueryDSL 기반 동적 쿼리 처리
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class ProductStockQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final QProductStockJpaEntity productStock =
            QProductStockJpaEntity.productStockJpaEntity;

    public ProductStockQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 상품 ID로 재고 조회
     *
     * @param productId 상품 ID
     * @return 재고 Entity
     */
    public Optional<ProductStockJpaEntity> findByProductId(Long productId) {
        ProductStockJpaEntity result =
                queryFactory
                        .selectFrom(productStock)
                        .where(productStock.productId.eq(productId))
                        .fetchOne();
        return Optional.ofNullable(result);
    }

    /**
     * 여러 상품 ID로 재고 일괄 조회
     *
     * @param productIds 상품 ID 목록
     * @return 재고 Entity 목록
     */
    public List<ProductStockJpaEntity> findByProductIds(List<Long> productIds) {
        return queryFactory
                .selectFrom(productStock)
                .where(productStock.productId.in(productIds))
                .fetch();
    }

    /**
     * 재고 ID로 조회
     *
     * @param productStockId 재고 ID
     * @return 재고 Entity
     */
    public Optional<ProductStockJpaEntity> findById(Long productStockId) {
        ProductStockJpaEntity result =
                queryFactory
                        .selectFrom(productStock)
                        .where(productStock.id.eq(productStockId))
                        .fetchOne();
        return Optional.ofNullable(result);
    }
}
