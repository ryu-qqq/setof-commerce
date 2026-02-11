package com.ryuqq.setof.storage.legacy.composite.web.user.repository;

import static com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity.legacyBrandEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupEntity.legacyProductGroupEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupImageEntity.legacyProductGroupImageEntity;
import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyUserFavoriteEntity.legacyUserFavoriteEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.user.dto.query.LegacyUserFavoriteSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.user.condition.LegacyWebUserFavoriteConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.user.dto.LegacyWebUserFavoriteQueryDto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * 레거시 찜 목록 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * <p>커서 기반 페이징 (lastFavoriteId) 지원.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebUserFavoriteQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebUserFavoriteConditionBuilder conditionBuilder;

    public LegacyWebUserFavoriteQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebUserFavoriteConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 찜 목록 조회 (커서 기반 페이징).
     *
     * <p>user_favorite + product_group + product_group_image + brand 조인.
     *
     * @param condition 검색 조건
     * @return 찜 목록
     */
    public List<LegacyWebUserFavoriteQueryDto> fetchUserFavorites(
            LegacyUserFavoriteSearchCondition condition) {
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebUserFavoriteQueryDto.class,
                                legacyUserFavoriteEntity.id,
                                legacyProductGroupEntity.id,
                                legacyProductGroupEntity.sellerId,
                                legacyProductGroupEntity.productGroupName,
                                legacyBrandEntity.id,
                                legacyBrandEntity.brandName,
                                legacyProductGroupImageEntity.imageUrl,
                                legacyProductGroupEntity.regularPrice.castToNum(BigDecimal.class),
                                legacyProductGroupEntity.currentPrice.castToNum(BigDecimal.class),
                                calculateDiscountRate(),
                                legacyProductGroupEntity.soldOutYn.stringValue(),
                                legacyProductGroupEntity.displayYn.stringValue(),
                                legacyUserFavoriteEntity.insertDate))
                .from(legacyUserFavoriteEntity)
                .innerJoin(legacyProductGroupEntity)
                .on(conditionBuilder.productGroupJoinCondition())
                .innerJoin(legacyProductGroupImageEntity)
                .on(conditionBuilder.productGroupImageJoinCondition())
                .innerJoin(legacyBrandEntity)
                .on(conditionBuilder.brandJoinCondition())
                .where(
                        conditionBuilder.userIdEq(condition.userId()),
                        conditionBuilder.notDeleted(),
                        conditionBuilder.notSoldOut(),
                        conditionBuilder.favoriteIdLt(condition.lastFavoriteId()))
                .orderBy(legacyUserFavoriteEntity.id.desc())
                .limit(condition.pageSize() + 1)
                .fetch();
    }

    /**
     * 찜 총 개수 조회.
     *
     * @param condition 검색 조건
     * @return 총 개수
     */
    public long countUserFavorites(LegacyUserFavoriteSearchCondition condition) {
        Long count =
                queryFactory
                        .select(legacyUserFavoriteEntity.count())
                        .from(legacyUserFavoriteEntity)
                        .innerJoin(legacyProductGroupEntity)
                        .on(conditionBuilder.productGroupJoinCondition())
                        .where(
                                conditionBuilder.userIdEq(condition.userId()),
                                conditionBuilder.notDeleted(),
                                conditionBuilder.notSoldOut())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 할인율 계산 Expression.
     *
     * <p>(regularPrice - currentPrice) / regularPrice * 100
     */
    private com.querydsl.core.types.dsl.NumberExpression<Integer> calculateDiscountRate() {
        return legacyProductGroupEntity
                .regularPrice
                .subtract(legacyProductGroupEntity.currentPrice)
                .multiply(100)
                .divide(legacyProductGroupEntity.regularPrice)
                .intValue();
    }
}
