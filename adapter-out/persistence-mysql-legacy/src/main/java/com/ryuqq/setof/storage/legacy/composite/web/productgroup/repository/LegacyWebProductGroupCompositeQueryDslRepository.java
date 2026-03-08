package com.ryuqq.setof.storage.legacy.composite.web.productgroup.repository;

import static com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity.legacyBrandEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductDeliveryEntity.legacyProductDeliveryEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductEntity.legacyProductEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupDetailDescriptionEntity.legacyProductGroupDetailDescriptionEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupEntity.legacyProductGroupEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupImageEntity.legacyProductGroupImageEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductNoticeEntity.legacyProductNoticeEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductOptionEntity.legacyProductOptionEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductRatingStatsEntity.legacyProductRatingStatsEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductScoreEntity.legacyProductScoreEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductStockEntity.legacyProductStockEntity;
import static com.ryuqq.setof.storage.legacy.seller.entity.QLegacySellerEntity.legacySellerEntity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.product.dto.query.LegacyProductGroupSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.productgroup.condition.LegacyWebProductGroupCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.productgroup.dto.LegacyWebProductGroupBasicQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.productgroup.dto.LegacyWebProductGroupThumbnailQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.productgroup.dto.LegacyWebProductImageQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.productgroup.dto.LegacyWebProductQueryDto;
import com.ryuqq.setof.storage.legacy.product.entity.LegacyProductEntity;
import com.ryuqq.setof.storage.legacy.product.entity.LegacyProductGroupDetailDescriptionEntity;
import com.ryuqq.setof.storage.legacy.product.entity.LegacyProductGroupImageEntity;
import com.ryuqq.setof.storage.legacy.product.entity.LegacyProductOptionEntity;
import com.ryuqq.setof.storage.legacy.product.entity.LegacyProductStockEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebProductGroupCompositeQueryDslRepository - 레거시 Web 상품그룹 Composite 조회 Repository.
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
     * 상품그룹 단건 기본 정보 조회 (fetchProductGroup - 쿼리 1).
     *
     * <p>product_group, seller, brand, category, product_delivery, product_notice,
     * product_group_detail_description, product_rating_stats 조인.
     *
     * @param productGroupId 상품그룹 ID
     * @return 기본 정보 Optional
     */
    public Optional<LegacyWebProductGroupBasicQueryDto> fetchBasicInfo(Long productGroupId) {
        LegacyWebProductGroupBasicQueryDto dto =
                queryFactory
                        .select(
                                Projections.constructor(
                                        LegacyWebProductGroupBasicQueryDto.class,
                                        legacyProductGroupEntity.id,
                                        legacyProductGroupEntity.productGroupName,
                                        legacySellerEntity.id,
                                        legacySellerEntity.sellerName,
                                        legacyBrandEntity.id,
                                        legacyBrandEntity.brandName,
                                        legacyBrandEntity.displayKoreanName,
                                        legacyBrandEntity.displayEnglishName,
                                        legacyBrandEntity.brandIconImageUrl,
                                        legacyProductGroupEntity.categoryId,
                                        legacyProductGroupEntity.path,
                                        legacyProductGroupEntity.regularPrice,
                                        legacyProductGroupEntity.currentPrice,
                                        legacyProductGroupEntity.salePrice,
                                        legacyProductGroupEntity.directDiscountRate,
                                        legacyProductGroupEntity.directDiscountPrice,
                                        legacyProductGroupEntity.discountRate,
                                        legacyProductGroupEntity.optionType.stringValue(),
                                        legacyProductGroupEntity.displayYn.stringValue(),
                                        legacyProductGroupEntity.soldOutYn.stringValue(),
                                        legacyProductGroupDetailDescriptionEntity.imageUrl,
                                        legacyProductDeliveryEntity.deliveryNotice,
                                        legacyProductDeliveryEntity.refundNotice,
                                        legacyProductRatingStatsEntity.averageRating.coalesce(0.0),
                                        legacyProductRatingStatsEntity.reviewCount.coalesce(0L),
                                        legacyProductGroupEntity.insertDate))
                        .from(legacyProductGroupEntity)
                        .innerJoin(legacySellerEntity)
                        .on(legacySellerEntity.id.eq(legacyProductGroupEntity.sellerId))
                        .innerJoin(legacyBrandEntity)
                        .on(legacyBrandEntity.id.eq(legacyProductGroupEntity.brandId))
                        .innerJoin(legacyProductDeliveryEntity)
                        .on(
                                legacyProductDeliveryEntity.productGroupId.eq(
                                        legacyProductGroupEntity.id))
                        .innerJoin(legacyProductNoticeEntity)
                        .on(
                                legacyProductNoticeEntity.productGroupId.eq(
                                        legacyProductGroupEntity.id))
                        .innerJoin(legacyProductGroupDetailDescriptionEntity)
                        .on(
                                legacyProductGroupDetailDescriptionEntity
                                        .productGroupId
                                        .eq(legacyProductGroupEntity.id)
                                        .and(
                                                legacyProductGroupDetailDescriptionEntity.deleteYn
                                                        .eq(
                                                                LegacyProductGroupDetailDescriptionEntity
                                                                        .Yn.N)))
                        .leftJoin(legacyProductRatingStatsEntity)
                        .on(legacyProductRatingStatsEntity.id.eq(legacyProductGroupEntity.id))
                        .where(conditionBuilder.productGroupIdEq(productGroupId))
                        .fetchOne();
        return Optional.ofNullable(dto);
    }

    /**
     * 개별 상품 + 옵션 목록 조회 (fetchProductGroup - 쿼리 2).
     *
     * <p>product, product_stock, product_option, option_group, option_detail 조인.
     *
     * @param productGroupId 상품그룹 ID
     * @return 개별 상품 목록
     */
    public List<LegacyWebProductQueryDto> fetchProducts(Long productGroupId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebProductQueryDto.class,
                                legacyProductEntity.productGroupId,
                                legacyProductEntity.id,
                                legacyProductStockEntity.stockQuantity,
                                legacyProductOptionEntity.additionalPrice.coalesce(0L),
                                legacyProductEntity.soldOutYn.stringValue(),
                                legacyProductEntity.displayYn.stringValue(),
                                legacyProductOptionEntity.optionGroupId.coalesce(0L),
                                legacyProductOptionEntity.optionGroupName.coalesce(""),
                                legacyProductOptionEntity.optionDetailId.coalesce(0L),
                                legacyProductOptionEntity.optionValue.coalesce("")))
                .from(legacyProductEntity)
                .innerJoin(legacyProductStockEntity)
                .on(
                        legacyProductStockEntity
                                .productId
                                .eq(legacyProductEntity.id)
                                .and(
                                        legacyProductStockEntity.deleteYn.eq(
                                                LegacyProductStockEntity.Yn.N)))
                .leftJoin(legacyProductOptionEntity)
                .on(
                        legacyProductOptionEntity
                                .productId
                                .eq(legacyProductEntity.id)
                                .and(
                                        legacyProductOptionEntity.deleteYn.eq(
                                                LegacyProductOptionEntity.Yn.N)))
                .where(
                        legacyProductEntity.productGroupId.eq(productGroupId),
                        legacyProductEntity.deleteYn.eq(LegacyProductEntity.Yn.N))
                .fetch();
    }

    /**
     * 상품그룹 이미지 목록 조회 (fetchProductGroup - 쿼리 3).
     *
     * <p>product_group_image 단독 조회.
     *
     * @param productGroupId 상품그룹 ID
     * @return 이미지 목록
     */
    public List<LegacyWebProductImageQueryDto> fetchImages(Long productGroupId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebProductImageQueryDto.class,
                                legacyProductGroupImageEntity.id,
                                legacyProductGroupImageEntity.productGroupImageType.stringValue(),
                                legacyProductGroupImageEntity.imageUrl))
                .from(legacyProductGroupImageEntity)
                .where(
                        legacyProductGroupImageEntity.productGroupId.eq(productGroupId),
                        legacyProductGroupImageEntity.deleteYn.eq(
                                LegacyProductGroupImageEntity.Yn.N))
                .fetch();
    }

    /**
     * 상품그룹 썸네일 목록 조회 (fetchProductGroups - 커서 페이징).
     *
     * <p>product_group, product_group_image(MAIN), brand, product_rating_stats, product_score 조인.
     *
     * @param condition 검색 조건
     * @return 썸네일 목록 (pageSize + 1개 조회)
     */
    public List<LegacyWebProductGroupThumbnailQueryDto> fetchProductGroupThumbnails(
            LegacyProductGroupSearchCondition condition) {
        List<OrderSpecifier<?>> orders = buildOrderSpecifiers(condition.orderType());

        return queryFactory
                .select(
                        Projections.constructor(
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
                        conditionBuilder.dynamicCursorCondition(condition),
                        conditionBuilder.onSaleProduct(),
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
     * 상품그룹 총 개수 조회 (fetchProductGroups - 카운트 쿼리).
     *
     * @param condition 검색 조건
     * @return 총 개수
     */
    public long fetchProductGroupCount(LegacyProductGroupSearchCondition condition) {
        Long count =
                queryFactory
                        .select(legacyProductGroupEntity.id.count())
                        .from(legacyProductGroupEntity)
                        .where(
                                conditionBuilder.sellerIdEq(condition.sellerId()),
                                conditionBuilder.categoryFilter(condition),
                                conditionBuilder.brandFilter(condition),
                                conditionBuilder.betweenPrice(
                                        condition.lowestPrice(), condition.highestPrice()),
                                conditionBuilder.onSaleProduct())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 찜 목록 상품그룹 썸네일 조회 (fetchProductGroupLikes).
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return 썸네일 목록
     */
    public List<LegacyWebProductGroupThumbnailQueryDto> fetchProductGroupsByIds(
            List<Long> productGroupIds) {
        return queryFactory
                .select(
                        Projections.constructor(
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
                        conditionBuilder.productGroupIdIn(productGroupIds),
                        conditionBuilder.onSaleProduct())
                .fetch();
    }

    /**
     * 브랜드별 상품그룹 썸네일 조회 (fetchProductGroupWithBrand - Redis 캐시 제거 후 직접 DB 조회).
     *
     * @param brandId 브랜드 ID
     * @param pageSize 페이지 크기
     * @return 썸네일 목록 (score DESC 정렬)
     */
    public List<LegacyWebProductGroupThumbnailQueryDto> fetchProductGroupsByBrand(
            Long brandId, int pageSize) {
        return queryFactory
                .select(
                        Projections.constructor(
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
                .where(conditionBuilder.brandIdEq(brandId))
                .orderBy(legacyProductScoreEntity.score.coalesce(0.0).desc())
                .limit(pageSize)
                .fetch();
    }

    /**
     * 셀러별 상품그룹 썸네일 조회 (fetchProductGroupWithSeller - Redis 캐시 제거 후 직접 DB 조회).
     *
     * @param sellerId 셀러 ID
     * @param pageSize 페이지 크기
     * @return 썸네일 목록 (score DESC 정렬)
     */
    public List<LegacyWebProductGroupThumbnailQueryDto> fetchProductGroupsBySeller(
            Long sellerId, int pageSize) {
        return queryFactory
                .select(
                        Projections.constructor(
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
                .where(conditionBuilder.sellerIdEq(sellerId))
                .orderBy(legacyProductScoreEntity.score.coalesce(0.0).desc())
                .limit(pageSize)
                .fetch();
    }

    /**
     * OrderType에 따른 정렬 기준 생성.
     *
     * @param orderType 정렬 타입
     * @return OrderSpecifier 목록
     */
    private List<OrderSpecifier<?>> buildOrderSpecifiers(String orderType) {
        if (orderType == null) {
            return List.of(legacyProductGroupEntity.id.desc());
        }
        return switch (orderType) {
            case "RECOMMEND" ->
                    List.of(
                            legacyProductScoreEntity.score.coalesce(0.0).desc(),
                            legacyProductGroupEntity.id.desc());
            case "REVIEW" ->
                    List.of(
                            legacyProductRatingStatsEntity.reviewCount.coalesce(0L).desc(),
                            legacyProductGroupEntity.id.desc());
            case "HIGH_RATING" ->
                    List.of(
                            legacyProductRatingStatsEntity.averageRating.coalesce(0.0).desc(),
                            legacyProductGroupEntity.id.desc());
            case "LOW_PRICE" ->
                    List.of(
                            legacyProductGroupEntity.salePrice.asc(),
                            legacyProductGroupEntity.id.desc());
            case "HIGH_PRICE" ->
                    List.of(
                            legacyProductGroupEntity.salePrice.desc(),
                            legacyProductGroupEntity.id.desc());
            case "LOW_DISCOUNT" ->
                    List.of(
                            legacyProductGroupEntity.discountRate.asc(),
                            legacyProductGroupEntity.id.desc());
            case "HIGH_DISCOUNT" ->
                    List.of(
                            legacyProductGroupEntity.discountRate.desc(),
                            legacyProductGroupEntity.id.desc());
            case "RECENT" ->
                    List.of(
                            legacyProductGroupEntity.insertDate.desc(),
                            legacyProductGroupEntity.id.desc());
            default -> List.of(legacyProductGroupEntity.id.desc());
        };
    }
}
