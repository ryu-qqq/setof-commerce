package com.ryuqq.setof.storage.legacy.composite.web.search.repository;

import static com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity.legacyBrandEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupEntity.legacyProductGroupEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupImageEntity.legacyProductGroupImageEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductRatingStatsEntity.legacyProductRatingStatsEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductScoreEntity.legacyProductScoreEntity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.search.dto.query.LegacySearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.search.condition.LegacyWebSearchCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.search.dto.LegacyWebSearchQueryDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebSearchCompositeQueryDslRepository - 레거시 Web 검색 Composite 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * <p>MySQL ngram FULLTEXT 검색을 지원합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebSearchCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebSearchCompositeConditionBuilder conditionBuilder;

    public LegacyWebSearchCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebSearchCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 키워드 검색 결과 목록 조회 (fetchResults - 커서 페이징).
     *
     * <p>product_group, product_group_image(MAIN), brand, product_rating_stats, product_score 조인.
     *
     * <p>searchWord가 있으면 MySQL ngram FULLTEXT 검색 적용.
     *
     * <p>pageSize + 1개 조회하여 hasNext 판별.
     *
     * @param condition 검색 조건
     * @return 검색 결과 목록
     */
    public List<LegacyWebSearchQueryDto> fetchSearchResults(LegacySearchCondition condition) {
        List<OrderSpecifier<?>> orders =
                conditionBuilder.buildOrderSpecifiers(condition.orderType());

        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebSearchQueryDto.class,
                                legacyProductGroupEntity.id,
                                legacyProductGroupEntity.sellerId,
                                legacyProductGroupEntity.productGroupName,
                                legacyBrandEntity.id,
                                legacyBrandEntity.brandName,
                                legacyBrandEntity.displayKoreanName,
                                legacyBrandEntity.displayEnglishName,
                                legacyBrandEntity.brandIconImageUrl,
                                legacyProductGroupImageEntity.imageUrl,
                                legacyProductGroupEntity.regularPrice,
                                legacyProductGroupEntity.currentPrice,
                                legacyProductGroupEntity.salePrice,
                                legacyProductGroupEntity.directDiscountRate,
                                legacyProductGroupEntity.directDiscountPrice,
                                legacyProductGroupEntity.discountRate,
                                legacyProductGroupEntity.insertDate,
                                legacyProductRatingStatsEntity.averageRating.coalesce(0.0),
                                legacyProductRatingStatsEntity.reviewCount.coalesce(0L),
                                legacyProductScoreEntity.score.coalesce(0.0),
                                legacyProductGroupEntity.displayYn.stringValue(),
                                legacyProductGroupEntity.soldOutYn.stringValue()))
                .from(legacyProductGroupEntity)
                .innerJoin(legacyProductGroupImageEntity)
                .on(
                        legacyProductGroupImageEntity
                                .productGroupId
                                .eq(legacyProductGroupEntity.id)
                                .and(conditionBuilder.mainImageCondition())
                                .and(conditionBuilder.imageNotDeleted()))
                .innerJoin(legacyBrandEntity)
                .on(legacyBrandEntity.id.eq(legacyProductGroupEntity.brandId))
                .leftJoin(legacyProductRatingStatsEntity)
                .on(legacyProductRatingStatsEntity.id.eq(legacyProductGroupEntity.id))
                .leftJoin(legacyProductScoreEntity)
                .on(legacyProductScoreEntity.id.eq(legacyProductGroupEntity.id))
                .where(
                        conditionBuilder.fullTextSearch(condition.searchWord()),
                        conditionBuilder.dynamicCursorCondition(condition),
                        conditionBuilder.sellerIdEq(condition.sellerId()),
                        conditionBuilder.categoryFilter(condition),
                        conditionBuilder.brandFilter(condition),
                        conditionBuilder.betweenPrice(
                                condition.lowestPrice(), condition.highestPrice()))
                .orderBy(orders.toArray(OrderSpecifier[]::new))
                .limit(condition.pageSize() + 1L)
                .fetch();
    }

    /**
     * 검색 결과 총 개수 조회 (fetchSearchCountQuery).
     *
     * <p>searchWord가 있으면 MySQL ngram FULLTEXT 검색 적용.
     *
     * @param condition 검색 조건
     * @return 총 개수
     */
    public long fetchSearchCount(LegacySearchCondition condition) {
        Long count =
                queryFactory
                        .select(legacyProductGroupEntity.id.count())
                        .from(legacyProductGroupEntity)
                        .innerJoin(legacyProductGroupImageEntity)
                        .on(
                                legacyProductGroupImageEntity
                                        .productGroupId
                                        .eq(legacyProductGroupEntity.id)
                                        .and(conditionBuilder.mainImageCondition())
                                        .and(conditionBuilder.imageNotDeleted()))
                        .innerJoin(legacyBrandEntity)
                        .on(legacyBrandEntity.id.eq(legacyProductGroupEntity.brandId))
                        .leftJoin(legacyProductRatingStatsEntity)
                        .on(legacyProductRatingStatsEntity.id.eq(legacyProductGroupEntity.id))
                        .leftJoin(legacyProductScoreEntity)
                        .on(legacyProductScoreEntity.id.eq(legacyProductGroupEntity.id))
                        .where(conditionBuilder.fullTextSearch(condition.searchWord()))
                        .fetchOne();
        return count != null ? count : 0L;
    }
}
