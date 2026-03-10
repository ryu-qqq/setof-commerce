package com.ryuqq.setof.storage.legacy.productgroup.adapter;

import static com.ryuqq.setof.storage.legacy.productgroup.entity.QLegacyProductGroupEntity.legacyProductGroupEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.application.payment.port.out.query.ProductPriceQueryPort;
import org.springframework.stereotype.Component;

/**
 * LegacyProductPriceQueryAdapter - Legacy product_group 가격 조회 어댑터.
 *
 * <p>결제 시 가격 검증을 위해 current_price를 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyProductPriceQueryAdapter implements ProductPriceQueryPort {

    private final JPAQueryFactory queryFactory;

    public LegacyProductPriceQueryAdapter(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public long getCurrentPrice(long productGroupId) {
        Integer price =
                queryFactory
                        .select(legacyProductGroupEntity.currentPrice)
                        .from(legacyProductGroupEntity)
                        .where(legacyProductGroupEntity.id.eq(productGroupId))
                        .fetchOne();

        if (price == null) {
            throw new IllegalArgumentException("상품 가격 조회 실패: productGroupId=" + productGroupId);
        }

        return price.longValue();
    }
}
