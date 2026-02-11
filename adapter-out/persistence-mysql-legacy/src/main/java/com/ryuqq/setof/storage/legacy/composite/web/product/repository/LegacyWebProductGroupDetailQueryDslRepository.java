package com.ryuqq.setof.storage.legacy.composite.web.product.repository;

import static com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity.legacyBrandEntity;
import static com.ryuqq.setof.storage.legacy.category.entity.QLegacyCategoryEntity.legacyCategoryEntity;
import static com.ryuqq.setof.storage.legacy.option.entity.QLegacyOptionDetailEntity.legacyOptionDetailEntity;
import static com.ryuqq.setof.storage.legacy.option.entity.QLegacyOptionGroupEntity.legacyOptionGroupEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductDeliveryEntity.legacyProductDeliveryEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductEntity.legacyProductEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupDetailDescriptionEntity.legacyProductGroupDetailDescriptionEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupEntity.legacyProductGroupEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupImageEntity.legacyProductGroupImageEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductOptionEntity.legacyProductOptionEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductStockEntity.legacyProductStockEntity;
import static com.ryuqq.setof.storage.legacy.review.entity.QLegacyProductRatingStatsEntity.legacyProductRatingStatsEntity;
import static com.ryuqq.setof.storage.legacy.seller.entity.QLegacySellerEntity.legacySellerEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.composite.web.product.dto.LegacyWebProductGroupBasicQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.product.dto.LegacyWebProductImageQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.product.dto.LegacyWebProductQueryDto;
import com.ryuqq.setof.storage.legacy.product.entity.LegacyProductGroupDetailDescriptionEntity;
import com.ryuqq.setof.storage.legacy.product.entity.LegacyProductGroupImageEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebProductGroupDetailQueryDslRepository - 레거시 웹 상품그룹 상세 조회 Repository.
 *
 * <p>상품그룹 상세 조회는 3개의 분리된 쿼리로 최적화되어 있습니다:
 *
 * <ul>
 *   <li>Query 1: 기본 정보 (8개 테이블 조인)
 *   <li>Query 2: 상품+옵션 (5개 테이블)
 *   <li>Query 3: 이미지 (1개 테이블)
 * </ul>
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebProductGroupDetailQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public LegacyWebProductGroupDetailQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * Query 1: 기본 정보 조회.
     *
     * <p>8개 테이블 조인: product_group, seller, category, brand, product_delivery, product_notice,
     * product_group_detail_description, product_rating_stats
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
                                        legacyCategoryEntity.id,
                                        legacyCategoryEntity.path,
                                        legacyProductGroupEntity.regularPrice,
                                        legacyProductGroupEntity.currentPrice,
                                        legacyProductGroupEntity.salePrice,
                                        legacyProductGroupEntity.optionType.stringValue(),
                                        legacyProductGroupEntity.displayYn.stringValue(),
                                        legacyProductGroupEntity.soldOutYn.stringValue(),
                                        legacyProductGroupDetailDescriptionEntity.imageUrl,
                                        legacyProductDeliveryEntity.deliveryNotice,
                                        legacyProductDeliveryEntity.refundNotice,
                                        new CaseBuilder()
                                                .when(
                                                        legacyProductRatingStatsEntity.averageRating
                                                                .isNull())
                                                .then(0.0)
                                                .otherwise(
                                                        legacyProductRatingStatsEntity
                                                                .averageRating),
                                        new CaseBuilder()
                                                .when(
                                                        legacyProductRatingStatsEntity.reviewCount
                                                                .isNull())
                                                .then(0L)
                                                .otherwise(
                                                        legacyProductRatingStatsEntity.reviewCount),
                                        legacyProductGroupEntity.createdAt))
                        .from(legacyProductGroupEntity)
                        .innerJoin(legacySellerEntity)
                        .on(legacySellerEntity.id.eq(legacyProductGroupEntity.sellerId))
                        .innerJoin(legacyCategoryEntity)
                        .on(legacyCategoryEntity.id.eq(legacyProductGroupEntity.categoryId))
                        .innerJoin(legacyBrandEntity)
                        .on(legacyBrandEntity.id.eq(legacyProductGroupEntity.brandId))
                        .innerJoin(legacyProductDeliveryEntity)
                        .on(
                                legacyProductDeliveryEntity.productGroupId.eq(
                                        legacyProductGroupEntity.id))
                        .innerJoin(legacyProductGroupDetailDescriptionEntity)
                        .on(
                                legacyProductGroupDetailDescriptionEntity.productGroupId.eq(
                                        legacyProductGroupEntity.id),
                                legacyProductGroupDetailDescriptionEntity.deleteYn.eq(
                                        LegacyProductGroupDetailDescriptionEntity.Yn.N))
                        .leftJoin(legacyProductRatingStatsEntity)
                        .on(legacyProductRatingStatsEntity.id.eq(legacyProductGroupEntity.id))
                        .where(legacyProductGroupEntity.id.eq(productGroupId))
                        .fetchOne();

        return Optional.ofNullable(dto);
    }

    /**
     * Query 2: 상품+옵션 조회.
     *
     * <p>5개 테이블 조인: product, product_stock, product_option, option_group, option_detail
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품 목록
     */
    public List<LegacyWebProductQueryDto> fetchProducts(Long productGroupId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebProductQueryDto.class,
                                legacyProductEntity.id,
                                legacyProductEntity.productGroupId,
                                legacyProductOptionEntity.additionalPrice.coalesce(0L).intValue(),
                                legacyProductStockEntity.stockQuantity.coalesce(0),
                                legacyProductEntity.soldOutYn.stringValue(),
                                legacyOptionGroupEntity.id,
                                legacyOptionGroupEntity.optionName.stringValue(),
                                legacyOptionDetailEntity.id,
                                legacyOptionDetailEntity.optionValue))
                .from(legacyProductEntity)
                .innerJoin(legacyProductStockEntity)
                .on(legacyProductStockEntity.productId.eq(legacyProductEntity.id))
                .leftJoin(legacyProductOptionEntity)
                .on(legacyProductOptionEntity.productId.eq(legacyProductEntity.id))
                .leftJoin(legacyOptionGroupEntity)
                .on(legacyOptionGroupEntity.id.eq(legacyProductOptionEntity.optionGroupId))
                .leftJoin(legacyOptionDetailEntity)
                .on(legacyOptionDetailEntity.id.eq(legacyProductOptionEntity.optionDetailId))
                .where(legacyProductEntity.productGroupId.eq(productGroupId))
                .fetch();
    }

    /**
     * Query 3: 이미지 조회.
     *
     * <p>1개 테이블: product_group_image
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
                                legacyProductGroupImageEntity.productGroupId,
                                legacyProductGroupImageEntity.productGroupImageType.stringValue(),
                                legacyProductGroupImageEntity.imageUrl))
                .from(legacyProductGroupImageEntity)
                .where(
                        legacyProductGroupImageEntity.productGroupId.eq(productGroupId),
                        legacyProductGroupImageEntity.deleteYn.eq(
                                LegacyProductGroupImageEntity.Yn.N))
                .fetch();
    }
}
