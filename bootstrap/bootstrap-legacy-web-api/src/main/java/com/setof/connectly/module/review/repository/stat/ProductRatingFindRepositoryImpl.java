package com.setof.connectly.module.review.repository.stat;

import static com.setof.connectly.module.review.entity.QProductRatingStats.productRatingStats;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.review.entity.ProductRatingStats;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRatingFindRepositoryImpl implements ProductRatingFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ProductRatingStats> fetchProductRatingStats(long productGroupId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(productRatingStats)
                        .where(productGroupIdEq(productGroupId))
                        .fetchFirst());
    }

    private BooleanExpression productGroupIdEq(long productGroupId) {
        return productRatingStats.id.eq(productGroupId);
    }
}
