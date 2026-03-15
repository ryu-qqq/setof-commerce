package com.ryuqq.setof.adapter.out.persistence.cart.adapter;

import static com.ryuqq.setof.adapter.out.persistence.product.entity.QProductJpaEntity.productJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.application.cart.port.out.query.CartStockQueryPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * CartStockQueryAdapter - 장바구니 재고 조회 어댑터.
 *
 * <p>CartStockQueryPort를 구현하여 새 스키마(setof) products 테이블에서 재고를 조회합니다.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>활성화 조건: persistence.legacy.cart.enabled=false (기본 활성)
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(
        name = "persistence.legacy.cart.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class CartStockQueryAdapter implements CartStockQueryPort {

    private final JPAQueryFactory queryFactory;

    public CartStockQueryAdapter(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public int fetchStockQuantity(long productId) {
        Integer quantity =
                queryFactory
                        .select(productJpaEntity.stockQuantity)
                        .from(productJpaEntity)
                        .where(productJpaEntity.id.eq(productId))
                        .fetchOne();
        return quantity != null ? quantity : 0;
    }
}
