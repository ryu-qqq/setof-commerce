package com.ryuqq.setof.storage.legacy.cart.repository;

import static com.ryuqq.setof.storage.legacy.productgroup.entity.QLegacyProductStockEntity.legacyProductStockEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyCartStockQueryDslRepository - 장바구니용 재고 조회 QueryDSL 레포지토리.
 *
 * <p>레거시 product_stock 테이블에서 상품 재고를 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyCartStockQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public LegacyCartStockQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Integer fetchStockQuantity(long productId) {
        return queryFactory
                .select(legacyProductStockEntity.stockQuantity)
                .from(legacyProductStockEntity)
                .where(legacyProductStockEntity.productId.eq(productId))
                .fetchOne();
    }
}
