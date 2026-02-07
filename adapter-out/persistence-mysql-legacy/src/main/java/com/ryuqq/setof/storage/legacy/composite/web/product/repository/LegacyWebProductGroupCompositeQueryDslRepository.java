package com.ryuqq.setof.storage.legacy.composite.web.product.repository;

import static com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity.legacyBrandEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupEntity.legacyProductGroupEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupImageEntity.legacyProductGroupImageEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductScoreEntity.legacyProductScoreEntity;
import static com.ryuqq.setof.storage.legacy.review.entity.QLegacyProductRatingStatsEntity.legacyProductRatingStatsEntity;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.product.dto.query.LegacyProductGroupSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.product.condition.LegacyWebProductGroupCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.product.dto.LegacyWebProductGroupThumbnailQueryDto;
import com.ryuqq.setof.storage.legacy.product.entity.LegacyProductGroupImageEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebProductGroupCompositeQueryDslRepository - 레거시 웹 상품그룹 Composite 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebProductGroupCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebProductGroupCompositeConditionBuilder conditionBuilder;

    public LegacyWebProductGroupCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebProductGroupCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 상품그룹 썸네일 목록 조회 (필터 + 페이징).
     *
     * <p>커서 기반 페이징과 다양한 필터를 지원합니다.
     *
     * @param condition 검색 조건
     * @return 상품그룹 썸네일 목록
     */
    public List<LegacyWebProductGroupThumbnailQueryDto> fetchProductGroupThumbnails(
            LegacyProductGroupSearchCondition condition) {
        JPAQuery<LegacyWebProductGroupThumbnailQueryDto> query = createBaseQuery();

        applyFilters(query, condition);
        applyOrderBy(query, condition.orderType());

        if (condition.pageSize() > 0) {
            query.limit(condition.pageSize() + 1);
        }

        return query.fetch();
    }

    /**
     * 브랜드별 상품그룹 목록 조회.
     *
     * @param brandId 브랜드 ID
     * @param pageSize 페이지 크기
     * @return 상품그룹 썸네일 목록
     */
    public List<LegacyWebProductGroupThumbnailQueryDto> fetchProductGroupsByBrand(
            Long brandId, int pageSize) {
        return createBaseQuery()
                .where(
                        conditionBuilder.brandIdEq(brandId),
                        conditionBuilder.active(),
                        mainImageCondition())
                .orderBy(legacyProductScoreEntity.score.desc().nullsLast())
                .limit(pageSize)
                .fetch();
    }

    /**
     * 셀러별 상품그룹 목록 조회.
     *
     * @param sellerId 셀러 ID
     * @param pageSize 페이지 크기
     * @return 상품그룹 썸네일 목록
     */
    public List<LegacyWebProductGroupThumbnailQueryDto> fetchProductGroupsBySeller(
            Long sellerId, int pageSize) {
        return createBaseQuery()
                .where(
                        conditionBuilder.sellerIdEq(sellerId),
                        conditionBuilder.active(),
                        mainImageCondition())
                .orderBy(legacyProductScoreEntity.score.desc().nullsLast())
                .limit(pageSize)
                .fetch();
    }

    /**
     * 최근 본 상품(ID 목록) 조회.
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return 상품그룹 썸네일 목록
     */
    public List<LegacyWebProductGroupThumbnailQueryDto> fetchProductGroupsByIds(
            List<Long> productGroupIds) {
        if (productGroupIds == null || productGroupIds.isEmpty()) {
            return List.of();
        }
        return createBaseQuery()
                .where(
                        conditionBuilder.productGroupIdIn(productGroupIds),
                        conditionBuilder.active(),
                        mainImageCondition())
                .fetch();
    }

    /**
     * 상품그룹 개수 조회.
     *
     * @param condition 검색 조건
     * @return 상품그룹 개수
     */
    public long countProductGroups(LegacyProductGroupSearchCondition condition) {
        Long count =
                queryFactory
                        .select(legacyProductGroupEntity.count())
                        .from(legacyProductGroupEntity)
                        .where(
                                conditionBuilder.sellerIdEq(condition.sellerId()),
                                conditionBuilder.brandIdEq(condition.brandId()),
                                conditionBuilder.categoryIdEq(condition.categoryId()),
                                conditionBuilder.categoryIdIn(condition.categoryIds()),
                                conditionBuilder.brandIdIn(condition.brandIds()),
                                conditionBuilder.priceBetween(
                                        condition.lowestPrice(), condition.highestPrice()),
                                conditionBuilder.active())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 기본 쿼리 생성.
     *
     * <p>5개 테이블 조인: product_group, product_group_image, brand, product_rating_stats, product_score
     */
    private JPAQuery<LegacyWebProductGroupThumbnailQueryDto> createBaseQuery() {
        return queryFactory
                .select(createProjection())
                .from(legacyProductGroupEntity)
                .innerJoin(legacyProductGroupImageEntity)
                .on(
                        legacyProductGroupImageEntity.productGroupId.eq(
                                legacyProductGroupEntity.id),
                        legacyProductGroupImageEntity.productGroupImageType.eq(
                                LegacyProductGroupImageEntity.ProductGroupImageType.MAIN),
                        legacyProductGroupImageEntity.deleteYn.eq(
                                LegacyProductGroupImageEntity.Yn.N))
                .innerJoin(legacyBrandEntity)
                .on(legacyBrandEntity.id.eq(legacyProductGroupEntity.brandId))
                .leftJoin(legacyProductRatingStatsEntity)
                .on(legacyProductRatingStatsEntity.id.eq(legacyProductGroupEntity.id))
                .leftJoin(legacyProductScoreEntity)
                .on(legacyProductScoreEntity.id.eq(legacyProductGroupEntity.id));
    }

    /** 필터 조건 적용. */
    private void applyFilters(
            JPAQuery<LegacyWebProductGroupThumbnailQueryDto> query,
            LegacyProductGroupSearchCondition condition) {
        query.where(
                conditionBuilder.sellerIdEq(condition.sellerId()),
                conditionBuilder.brandIdEq(condition.brandId()),
                conditionBuilder.categoryIdEq(condition.categoryId()),
                conditionBuilder.categoryIdIn(condition.categoryIds()),
                conditionBuilder.brandIdIn(condition.brandIds()),
                conditionBuilder.priceBetween(condition.lowestPrice(), condition.highestPrice()),
                conditionBuilder.active(),
                mainImageCondition());

        if (condition.hasCursor()) {
            applyCursorCondition(query, condition);
        }
    }

    /** 커서 페이징 조건 적용. */
    private void applyCursorCondition(
            JPAQuery<LegacyWebProductGroupThumbnailQueryDto> query,
            LegacyProductGroupSearchCondition condition) {
        String orderType = condition.orderType();
        String cursorValue = condition.cursorValue();
        Long lastDomainId = condition.lastDomainId();

        BooleanExpression cursorCondition =
                switch (orderType != null ? orderType : "RECENT") {
                    case "HIGH_PRICE" ->
                            legacyProductGroupEntity
                                    .salePrice
                                    .lt(Integer.parseInt(cursorValue))
                                    .or(
                                            legacyProductGroupEntity
                                                    .salePrice
                                                    .eq(Integer.parseInt(cursorValue))
                                                    .and(
                                                            legacyProductGroupEntity.id.loe(
                                                                    lastDomainId)));
                    case "LOW_PRICE" ->
                            legacyProductGroupEntity
                                    .salePrice
                                    .gt(Integer.parseInt(cursorValue))
                                    .or(
                                            legacyProductGroupEntity
                                                    .salePrice
                                                    .eq(Integer.parseInt(cursorValue))
                                                    .and(
                                                            legacyProductGroupEntity.id.loe(
                                                                    lastDomainId)));
                    case "HIGH_RATING" ->
                            legacyProductRatingStatsEntity
                                    .averageRating
                                    .lt(Double.parseDouble(cursorValue))
                                    .or(
                                            legacyProductRatingStatsEntity
                                                    .averageRating
                                                    .eq(Double.parseDouble(cursorValue))
                                                    .and(
                                                            legacyProductGroupEntity.id.loe(
                                                                    lastDomainId)));
                    case "REVIEW" ->
                            legacyProductRatingStatsEntity
                                    .reviewCount
                                    .lt(Long.parseLong(cursorValue))
                                    .or(
                                            legacyProductRatingStatsEntity
                                                    .reviewCount
                                                    .eq(Long.parseLong(cursorValue))
                                                    .and(
                                                            legacyProductGroupEntity.id.loe(
                                                                    lastDomainId)));
                    case "RECOMMEND" ->
                            legacyProductScoreEntity
                                    .score
                                    .lt(Double.parseDouble(cursorValue))
                                    .or(
                                            legacyProductScoreEntity
                                                    .score
                                                    .eq(Double.parseDouble(cursorValue))
                                                    .and(
                                                            legacyProductGroupEntity.id.loe(
                                                                    lastDomainId)));
                    default -> // RECENT
                            legacyProductGroupEntity.id.lt(lastDomainId);
                };

        query.where(cursorCondition);
    }

    /** 정렬 조건 적용. */
    private void applyOrderBy(
            JPAQuery<LegacyWebProductGroupThumbnailQueryDto> query, String orderType) {
        OrderSpecifier<?> orderSpecifier =
                switch (orderType != null ? orderType : "RECENT") {
                    case "HIGH_PRICE" -> legacyProductGroupEntity.salePrice.desc();
                    case "LOW_PRICE" -> legacyProductGroupEntity.salePrice.asc();
                    case "HIGH_RATING" ->
                            legacyProductRatingStatsEntity.averageRating.desc().nullsLast();
                    case "REVIEW" -> legacyProductRatingStatsEntity.reviewCount.desc().nullsLast();
                    case "RECOMMEND" -> legacyProductScoreEntity.score.desc().nullsLast();
                    default -> // RECENT
                            legacyProductGroupEntity.createdAt.desc();
                };

        query.orderBy(orderSpecifier, legacyProductGroupEntity.id.desc());
    }

    /** MAIN 이미지만 조회하는 조건. */
    private BooleanExpression mainImageCondition() {
        return legacyProductGroupImageEntity.productGroupImageType.eq(
                LegacyProductGroupImageEntity.ProductGroupImageType.MAIN);
    }

    /**
     * Projections.constructor()로 Projection 생성.
     *
     * <p>@QueryProjection 대신 사용.
     */
    private ConstructorExpression<LegacyWebProductGroupThumbnailQueryDto> createProjection() {
        return Projections.constructor(
                LegacyWebProductGroupThumbnailQueryDto.class,
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
                legacyProductGroupEntity.createdAt,
                new CaseBuilder()
                        .when(legacyProductRatingStatsEntity.averageRating.isNull())
                        .then(0.0)
                        .otherwise(legacyProductRatingStatsEntity.averageRating),
                new CaseBuilder()
                        .when(legacyProductRatingStatsEntity.reviewCount.isNull())
                        .then(0L)
                        .otherwise(legacyProductRatingStatsEntity.reviewCount),
                legacyProductScoreEntity.score,
                legacyProductGroupEntity.displayYn.stringValue(),
                legacyProductGroupEntity.soldOutYn.stringValue());
    }
}
