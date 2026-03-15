package com.ryuqq.setof.adapter.out.persistence.composite.productgroup.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.brand.entity.QBrandJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.category.entity.QCategoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.composite.productgroup.condition.CompositionProductConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.composite.productgroup.condition.ProductGroupCompositeConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.entity.QImageVariantJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.entity.QProductJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.entity.QProductOptionMappingJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QProductGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QSellerOptionGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QSellerOptionValueJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.QDescriptionImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.QProductGroupDescriptionJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupimage.entity.QProductGroupImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupprice.entity.QProductGroupPriceJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.QProductNoticeEntryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.QProductNoticeJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.refundpolicy.entity.QRefundPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.entity.QShippingPolicyJpaEntity;
import com.ryuqq.setof.application.product.dto.response.ProductOptionMappingResult;
import com.ryuqq.setof.application.product.dto.response.ProductResult;
import com.ryuqq.setof.application.productgroup.dto.composite.OptionGroupSummaryResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeQueryResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupEnrichmentResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupExcelBaseBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListCompositeResult;
import com.ryuqq.setof.application.productgroupdescription.dto.response.DescriptionImageResult;
import com.ryuqq.setof.application.productgroupimage.dto.response.ImageWithVariantsResult;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeEntryResult;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResult;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResult;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupOffsetSearchCriteria;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSortKey;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

/** ProductGroup Composition QueryDSL Repository. 크로스 도메인 JOIN 조회용. */
@SuppressWarnings("PMD.ExcessiveImports")
@Repository
public class ProductGroupCompositeQueryDslRepository {

    private static final QProductGroupJpaEntity pg = QProductGroupJpaEntity.productGroupJpaEntity;
    private static final QSellerJpaEntity seller = QSellerJpaEntity.sellerJpaEntity;
    private static final QBrandJpaEntity brand = QBrandJpaEntity.brandJpaEntity;
    private static final QCategoryJpaEntity category = QCategoryJpaEntity.categoryJpaEntity;
    private static final QProductGroupImageJpaEntity pgImage =
            QProductGroupImageJpaEntity.productGroupImageJpaEntity;
    private static final QProductJpaEntity product = QProductJpaEntity.productJpaEntity;
    private static final QSellerOptionGroupJpaEntity optionGroup =
            QSellerOptionGroupJpaEntity.sellerOptionGroupJpaEntity;
    private static final QSellerOptionValueJpaEntity optionValue =
            QSellerOptionValueJpaEntity.sellerOptionValueJpaEntity;
    private static final QShippingPolicyJpaEntity shippingPolicy =
            QShippingPolicyJpaEntity.shippingPolicyJpaEntity;
    private static final QRefundPolicyJpaEntity refundPolicy =
            QRefundPolicyJpaEntity.refundPolicyJpaEntity;
    private static final QProductOptionMappingJpaEntity optionMapping =
            QProductOptionMappingJpaEntity.productOptionMappingJpaEntity;
    private static final QProductGroupDescriptionJpaEntity description =
            QProductGroupDescriptionJpaEntity.productGroupDescriptionJpaEntity;
    private static final QProductNoticeJpaEntity notice =
            QProductNoticeJpaEntity.productNoticeJpaEntity;
    private static final QImageVariantJpaEntity imageVariant =
            QImageVariantJpaEntity.imageVariantJpaEntity;
    private static final QProductNoticeEntryJpaEntity noticeEntry =
            QProductNoticeEntryJpaEntity.productNoticeEntryJpaEntity;
    private static final QDescriptionImageJpaEntity descriptionImage =
            QDescriptionImageJpaEntity.descriptionImageJpaEntity;
    private static final QProductGroupPriceJpaEntity pgPrice =
            QProductGroupPriceJpaEntity.productGroupPriceJpaEntity;

    private final JPAQueryFactory queryFactory;
    private final ProductGroupCompositeConditionBuilder conditionBuilder;
    private final CompositionProductConditionBuilder productConditionBuilder;

    public ProductGroupCompositeQueryDslRepository(
            JPAQueryFactory queryFactory,
            ProductGroupCompositeConditionBuilder conditionBuilder,
            CompositionProductConditionBuilder productConditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
        this.productConditionBuilder = productConditionBuilder;
    }

    /** 단건 Composite 조회 (목록용 기본 데이터). */
    public Optional<ProductGroupListCompositeResult> findCompositeById(Long productGroupId) {
        Tuple row =
                queryFactory
                        .select(
                                pg.id,
                                pg.sellerId,
                                seller.sellerName,
                                pg.brandId,
                                brand.brandName,
                                brand.displayKoreanName,
                                brand.displayEnglishName,
                                brand.brandIconImageUrl,
                                pg.categoryId,
                                category.categoryName,
                                category.path,
                                category.categoryDepth,
                                pg.productGroupName,
                                pg.optionType,
                                pg.status,
                                pg.regularPrice,
                                pg.currentPrice,
                                pgPrice.salePrice,
                                pgPrice.discountRate,
                                pg.createdAt,
                                pg.updatedAt)
                        .from(pg)
                        .leftJoin(seller)
                        .on(pg.sellerId.eq(seller.id))
                        .leftJoin(brand)
                        .on(pg.brandId.eq(brand.id))
                        .leftJoin(category)
                        .on(pg.categoryId.eq(category.id))
                        .leftJoin(pgPrice)
                        .on(pgPrice.productGroupId.eq(pg.id))
                        .where(
                                conditionBuilder.idEq(productGroupId),
                                conditionBuilder.statusNotDeleted())
                        .fetchOne();

        if (row == null) {
            return Optional.empty();
        }

        String thumbnailUrl = findThumbnailUrl(productGroupId);
        int productCount = countProducts(productGroupId);

        return Optional.of(toListCompositeResult(row, thumbnailUrl, productCount));
    }

    /** 목록 Composite 조회. */
    public List<ProductGroupListCompositeResult> findCompositeByCriteria(
            ProductGroupOffsetSearchCriteria criteria) {

        List<Tuple> rows =
                queryFactory
                        .select(
                                pg.id,
                                pg.sellerId,
                                seller.sellerName,
                                pg.brandId,
                                brand.brandName,
                                brand.displayKoreanName,
                                brand.displayEnglishName,
                                brand.brandIconImageUrl,
                                pg.categoryId,
                                category.categoryName,
                                category.path,
                                category.categoryDepth,
                                pg.productGroupName,
                                pg.optionType,
                                pg.status,
                                pg.regularPrice,
                                pg.currentPrice,
                                pgPrice.salePrice,
                                pgPrice.discountRate,
                                pg.createdAt,
                                pg.updatedAt)
                        .from(pg)
                        .leftJoin(seller)
                        .on(pg.sellerId.eq(seller.id))
                        .leftJoin(brand)
                        .on(pg.brandId.eq(brand.id))
                        .leftJoin(category)
                        .on(pg.categoryId.eq(category.id))
                        .leftJoin(pgPrice)
                        .on(pgPrice.productGroupId.eq(pg.id))
                        .where(
                                conditionBuilder.idIn(criteria.productGroupIds()),
                                conditionBuilder.sellerIdIn(criteria.sellerIds()),
                                conditionBuilder.brandIdIn(criteria.brandIds()),
                                conditionBuilder.categoryIdIn(criteria.categoryIds()),
                                conditionBuilder.statusIn(criteria),
                                conditionBuilder.searchCondition(criteria),
                                conditionBuilder.statusNotDeleted())
                        .orderBy(resolveOrderSpecifier(criteria))
                        .offset(criteria.offset())
                        .limit(criteria.size())
                        .fetch();

        if (rows.isEmpty()) {
            return List.of();
        }

        List<Long> pgIds = rows.stream().map(r -> r.get(pg.id)).toList();
        Map<Long, String> thumbnailMap = findThumbnailUrls(pgIds);
        Map<Long, Integer> productCountMap = countProductsByGroupIds(pgIds);

        List<ProductGroupListCompositeResult> results = new ArrayList<>();
        for (Tuple row : rows) {
            Long pgId = row.get(pg.id);
            results.add(
                    toListCompositeResult(
                            row,
                            thumbnailMap.getOrDefault(pgId, null),
                            productCountMap.getOrDefault(pgId, 0)));
        }
        return results;
    }

    /** 조건별 카운트. */
    public long countByCriteria(ProductGroupOffsetSearchCriteria criteria) {
        Long count =
                queryFactory
                        .select(pg.count())
                        .from(pg)
                        .where(
                                conditionBuilder.idIn(criteria.productGroupIds()),
                                conditionBuilder.sellerIdIn(criteria.sellerIds()),
                                conditionBuilder.brandIdIn(criteria.brandIds()),
                                conditionBuilder.categoryIdIn(criteria.categoryIds()),
                                conditionBuilder.statusIn(criteria),
                                conditionBuilder.searchCondition(criteria),
                                conditionBuilder.statusNotDeleted())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /** 가격 + 옵션 Enrichment 배치 조회. */
    public List<ProductGroupEnrichmentResult> findEnrichmentsByProductGroupIds(
            List<Long> productGroupIds) {
        if (productGroupIds == null || productGroupIds.isEmpty()) {
            return List.of();
        }

        List<Tuple> priceTuples =
                queryFactory
                        .select(
                                product.productGroupId,
                                product.currentPrice.min(),
                                product.currentPrice.max(),
                                product.discountRate.max())
                        .from(product)
                        .where(
                                productConditionBuilder.productGroupIdIn(productGroupIds),
                                productConditionBuilder.statusNotDeleted())
                        .groupBy(product.productGroupId)
                        .fetch();

        Map<Long, int[]> priceMap = new LinkedHashMap<>();
        for (Tuple t : priceTuples) {
            Long pgId = t.get(product.productGroupId);
            priceMap.put(
                    pgId,
                    new int[] {
                        safeInt(t.get(product.currentPrice.min())),
                        safeInt(t.get(product.currentPrice.max())),
                        safeInt(t.get(product.discountRate.max()))
                    });
        }

        Map<Long, List<OptionGroupSummaryResult>> optionMap =
                findOptionSummariesByProductGroupIds(productGroupIds);

        List<ProductGroupEnrichmentResult> results = new ArrayList<>();
        for (Long pgId : productGroupIds) {
            int[] prices = priceMap.getOrDefault(pgId, new int[] {0, 0, 0});
            List<OptionGroupSummaryResult> options = optionMap.getOrDefault(pgId, List.of());
            results.add(
                    new ProductGroupEnrichmentResult(
                            pgId, prices[0], prices[1], prices[2], options));
        }
        return results;
    }

    /**
     * 상세용 Composite 조회 (1:1 관계 통합).
     *
     * <p>PG + Seller + Brand + Category + ShippingPolicy + RefundPolicy + Description(header) +
     * Notice(header)를 단일 쿼리로 조회합니다. 1:N 관계(Images, NoticeEntries, DescriptionImages 등)는 별도 배치 쿼리로
     * 처리합니다.
     */
    public Optional<ProductGroupDetailCompositeQueryResult> findDetailCompositeById(
            Long productGroupId) {

        Tuple row =
                queryFactory
                        .select(
                                pg.id,
                                pg.sellerId,
                                seller.sellerName,
                                pg.brandId,
                                brand.brandName,
                                brand.brandIconImageUrl,
                                pg.categoryId,
                                category.categoryName,
                                category.path,
                                pg.productGroupName,
                                pg.optionType,
                                pg.status,
                                pg.createdAt,
                                pg.updatedAt,
                                shippingPolicy.id,
                                shippingPolicy.policyName,
                                shippingPolicy.defaultPolicy,
                                shippingPolicy.active,
                                shippingPolicy.shippingFeeType,
                                shippingPolicy.baseFee,
                                shippingPolicy.freeThreshold,
                                shippingPolicy.createdAt,
                                refundPolicy.id,
                                refundPolicy.policyName,
                                refundPolicy.defaultPolicy,
                                refundPolicy.active,
                                refundPolicy.returnPeriodDays,
                                refundPolicy.exchangePeriodDays,
                                refundPolicy.createdAt,
                                description.id,
                                description.content,
                                description.cdnPath,
                                notice.id,
                                notice.createdAt,
                                notice.updatedAt)
                        .from(pg)
                        .leftJoin(seller)
                        .on(pg.sellerId.eq(seller.id))
                        .leftJoin(brand)
                        .on(pg.brandId.eq(brand.id))
                        .leftJoin(category)
                        .on(pg.categoryId.eq(category.id))
                        .leftJoin(shippingPolicy)
                        .on(pg.shippingPolicyId.eq(shippingPolicy.id))
                        .leftJoin(refundPolicy)
                        .on(pg.refundPolicyId.eq(refundPolicy.id))
                        .leftJoin(description)
                        .on(
                                description
                                        .productGroupId
                                        .eq(pg.id)
                                        .and(description.deletedAt.isNull()))
                        .leftJoin(notice)
                        .on(notice.productGroupId.eq(pg.id))
                        .where(
                                conditionBuilder.idEq(productGroupId),
                                conditionBuilder.statusNotDeleted())
                        .fetchOne();

        if (row == null) {
            return Optional.empty();
        }

        ShippingPolicyResult shippingResult = toShippingPolicyResult(row);
        RefundPolicyResult refundResult = toRefundPolicyResult(row);

        return Optional.of(
                new ProductGroupDetailCompositeQueryResult(
                        row.get(pg.id),
                        row.get(pg.sellerId),
                        row.get(seller.sellerName),
                        row.get(pg.brandId),
                        row.get(brand.brandName),
                        row.get(brand.brandIconImageUrl),
                        row.get(pg.categoryId),
                        row.get(category.categoryName),
                        row.get(category.path),
                        row.get(pg.productGroupName),
                        row.get(pg.optionType),
                        row.get(pg.status),
                        row.get(pg.createdAt),
                        row.get(pg.updatedAt),
                        shippingResult,
                        refundResult,
                        row.get(description.id),
                        row.get(description.content),
                        row.get(description.cdnPath),
                        row.get(notice.id),
                        row.get(notice.createdAt),
                        row.get(notice.updatedAt)));
    }

    /**
     * 이미지 + Variant 통합 조회.
     *
     * <p>product_group_images LEFT JOIN image_variants로 이미지와 변환된 variant를 한 번에 조회합니다. 1:N이지만 이미지당
     * variant가 최대 4개이므로 row 폭발 위험이 없습니다.
     */
    public List<ImageWithVariantsResult> findImagesWithVariantsByProductGroupId(
            Long productGroupId) {
        List<Tuple> rows =
                queryFactory
                        .select(
                                pgImage.id,
                                pgImage.imageType,
                                pgImage.imageUrl,
                                pgImage.sortOrder,
                                imageVariant.variantType,
                                imageVariant.variantUrl,
                                imageVariant.width,
                                imageVariant.height)
                        .from(pgImage)
                        .leftJoin(imageVariant)
                        .on(
                                imageVariant
                                        .sourceImageId
                                        .eq(pgImage.id)
                                        .and(imageVariant.sourceType.eq("PRODUCT_GROUP"))
                                        .and(imageVariant.deletedAt.isNull()))
                        .where(
                                pgImage.productGroupId.eq(productGroupId),
                                pgImage.deletedAt.isNull())
                        .orderBy(pgImage.sortOrder.asc())
                        .fetch();

        Map<Long, ImageWithVariantsResult> imageMap = new LinkedHashMap<>();
        for (Tuple t : rows) {
            Long imageId = t.get(pgImage.id);
            imageMap.computeIfAbsent(
                    imageId,
                    k ->
                            new ImageWithVariantsResult(
                                    imageId,
                                    t.get(pgImage.imageType),
                                    t.get(pgImage.imageUrl),
                                    safeInt(t.get(pgImage.sortOrder)),
                                    new ArrayList<>()));

            String variantType = t.get(imageVariant.variantType);
            if (variantType != null) {
                imageMap.get(imageId)
                        .variants()
                        .add(
                                new ImageWithVariantsResult.VariantInfo(
                                        variantType,
                                        t.get(imageVariant.variantUrl),
                                        t.get(imageVariant.width),
                                        t.get(imageVariant.height)));
            }
        }
        return new ArrayList<>(imageMap.values());
    }

    /** 고시정보 항목 배치 조회. */
    public List<ProductNoticeEntryResult> findNoticeEntriesByNoticeId(Long noticeId) {
        if (noticeId == null) {
            return List.of();
        }
        return queryFactory
                .select(noticeEntry.id, noticeEntry.fieldName, noticeEntry.fieldValue)
                .from(noticeEntry)
                .where(noticeEntry.productNoticeId.eq(noticeId), noticeEntry.deletedAt.isNull())
                .orderBy(noticeEntry.sortOrder.asc())
                .fetch()
                .stream()
                .map(
                        t ->
                                new ProductNoticeEntryResult(
                                        t.get(noticeEntry.id), null, t.get(noticeEntry.fieldValue)))
                .toList();
    }

    /** 상세설명 이미지 배치 조회. */
    public List<DescriptionImageResult> findDescriptionImagesByDescriptionId(Long descriptionId) {
        if (descriptionId == null) {
            return List.of();
        }
        return queryFactory
                .select(descriptionImage.id, descriptionImage.imageUrl, descriptionImage.sortOrder)
                .from(descriptionImage)
                .where(descriptionImage.productGroupDescriptionId.eq(descriptionId))
                .orderBy(descriptionImage.sortOrder.asc())
                .fetch()
                .stream()
                .map(
                        t ->
                                new DescriptionImageResult(
                                        t.get(descriptionImage.id),
                                        t.get(descriptionImage.imageUrl),
                                        safeInt(t.get(descriptionImage.sortOrder))))
                .toList();
    }

    /** 엑셀용 통합 Composite: base + 가격 enrichment + description cdnUrl + count. */
    public ProductGroupExcelBaseBundle findExcelBaseBundleByCriteria(
            ProductGroupOffsetSearchCriteria criteria) {

        List<ProductGroupListCompositeResult> baseComposites = findCompositeByCriteria(criteria);
        long totalElements = countByCriteria(criteria);

        if (baseComposites.isEmpty()) {
            return new ProductGroupExcelBaseBundle(List.of(), Map.of(), 0);
        }

        List<Long> pgIds =
                baseComposites.stream().map(ProductGroupListCompositeResult::id).toList();

        List<Tuple> priceTuples =
                queryFactory
                        .select(
                                product.productGroupId,
                                product.currentPrice.min(),
                                product.currentPrice.max(),
                                product.discountRate.max())
                        .from(product)
                        .where(
                                productConditionBuilder.productGroupIdIn(pgIds),
                                productConditionBuilder.statusNotDeleted())
                        .groupBy(product.productGroupId)
                        .fetch();

        Map<Long, int[]> priceMap = new LinkedHashMap<>();
        for (Tuple t : priceTuples) {
            Long pgId = t.get(product.productGroupId);
            priceMap.put(
                    pgId,
                    new int[] {
                        safeInt(t.get(product.currentPrice.min())),
                        safeInt(t.get(product.currentPrice.max())),
                        safeInt(t.get(product.discountRate.max()))
                    });
        }

        Map<Long, List<OptionGroupSummaryResult>> optionMap =
                findOptionSummariesByProductGroupIds(pgIds);

        List<ProductGroupListCompositeResult> enriched =
                baseComposites.stream()
                        .map(
                                base -> {
                                    int[] prices =
                                            priceMap.getOrDefault(base.id(), new int[] {0, 0, 0});
                                    List<OptionGroupSummaryResult> options =
                                            optionMap.getOrDefault(base.id(), List.of());
                                    return base.withEnrichment(
                                            prices[0], prices[1], prices[2], options);
                                })
                        .toList();

        Map<Long, String> cdnUrlMap =
                queryFactory
                        .select(description.productGroupId, description.cdnPath)
                        .from(description)
                        .where(description.productGroupId.in(pgIds))
                        .fetch()
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        t -> t.get(description.productGroupId),
                                        t ->
                                                t.get(description.cdnPath) != null
                                                        ? t.get(description.cdnPath)
                                                        : "",
                                        (a, b) -> a));

        return new ProductGroupExcelBaseBundle(enriched, cdnUrlMap, totalElements);
    }

    /**
     * 상품 + 옵션 매핑(이름 해석 포함) 배치 조회.
     *
     * <p>products LEFT JOIN product_option_mappings LEFT JOIN seller_option_values LEFT JOIN
     * seller_option_groups.
     */
    public Map<Long, List<ProductResult>> findProductsWithOptionNamesByProductGroupIds(
            List<Long> productGroupIds) {
        if (productGroupIds == null || productGroupIds.isEmpty()) {
            return Map.of();
        }

        List<Tuple> productTuples =
                queryFactory
                        .select(
                                product.id,
                                product.productGroupId,
                                product.skuCode,
                                product.regularPrice,
                                product.currentPrice,
                                product.salePrice,
                                product.discountRate,
                                product.stockQuantity,
                                product.status,
                                product.sortOrder,
                                product.createdAt,
                                product.updatedAt)
                        .from(product)
                        .where(
                                productConditionBuilder.productGroupIdIn(productGroupIds),
                                productConditionBuilder.statusNotDeleted())
                        .orderBy(product.sortOrder.asc())
                        .fetch();

        if (productTuples.isEmpty()) {
            return Map.of();
        }

        List<Long> productIds = productTuples.stream().map(t -> t.get(product.id)).toList();

        List<Tuple> mappingTuples =
                queryFactory
                        .select(
                                optionMapping.id,
                                optionMapping.productId,
                                optionMapping.sellerOptionValueId,
                                optionGroup.optionGroupName,
                                optionValue.optionValueName)
                        .from(optionMapping)
                        .leftJoin(optionValue)
                        .on(
                                optionMapping
                                        .sellerOptionValueId
                                        .eq(optionValue.id)
                                        .and(optionValue.deleted.isFalse()))
                        .leftJoin(optionGroup)
                        .on(
                                optionValue
                                        .sellerOptionGroupId
                                        .eq(optionGroup.id)
                                        .and(optionGroup.deleted.isFalse()))
                        .where(
                                optionMapping.productId.in(productIds),
                                optionMapping.deleted.isFalse())
                        .fetch();

        Map<Long, List<ProductOptionMappingResult>> mappingsByProductId = new LinkedHashMap<>();
        for (Tuple t : mappingTuples) {
            Long prodId = t.get(optionMapping.productId);
            mappingsByProductId
                    .computeIfAbsent(prodId, k -> new ArrayList<>())
                    .add(
                            ProductOptionMappingResult.withOptionNames(
                                    t.get(optionMapping.id),
                                    prodId,
                                    t.get(optionMapping.sellerOptionValueId),
                                    t.get(optionGroup.optionGroupName),
                                    t.get(optionValue.optionValueName)));
        }

        Map<Long, List<ProductResult>> result = new LinkedHashMap<>();
        for (Tuple t : productTuples) {
            Long prodId = t.get(product.id);
            Long pgId = t.get(product.productGroupId);
            List<ProductOptionMappingResult> mappings =
                    mappingsByProductId.getOrDefault(prodId, List.of());

            Integer salePriceRaw = t.get(product.salePrice);
            ProductResult productResult =
                    new ProductResult(
                            prodId,
                            pgId,
                            t.get(product.skuCode),
                            safeInt(t.get(product.regularPrice)),
                            safeInt(t.get(product.currentPrice)),
                            salePriceRaw,
                            safeInt(t.get(product.discountRate)),
                            safeInt(t.get(product.stockQuantity)),
                            t.get(product.status),
                            safeInt(t.get(product.sortOrder)),
                            mappings,
                            t.get(product.createdAt),
                            t.get(product.updatedAt));

            result.computeIfAbsent(pgId, k -> new ArrayList<>()).add(productResult);
        }

        return result;
    }

    /**
     * 썸네일 Variant URL 배치 조회.
     *
     * <p>product_group_images(THUMBNAIL) LEFT JOIN image_variants(MEDIUM_WEBP)로 productGroupId →
     * variant URL 맵을 한 방에 조회합니다. variant가 없는 상품그룹은 맵에 포함되지 않습니다.
     */
    public Map<Long, String> findThumbnailVariantUrlsByProductGroupIds(List<Long> productGroupIds) {
        if (productGroupIds == null || productGroupIds.isEmpty()) {
            return Map.of();
        }

        List<Tuple> rows =
                queryFactory
                        .select(pgImage.productGroupId, imageVariant.variantUrl)
                        .from(pgImage)
                        .innerJoin(imageVariant)
                        .on(
                                imageVariant
                                        .sourceImageId
                                        .eq(pgImage.id)
                                        .and(imageVariant.sourceType.eq("PRODUCT_GROUP"))
                                        .and(imageVariant.variantType.eq("MEDIUM_WEBP"))
                                        .and(imageVariant.deletedAt.isNull()))
                        .where(
                                pgImage.productGroupId.in(productGroupIds),
                                pgImage.imageType.eq("THUMBNAIL"),
                                pgImage.deletedAt.isNull())
                        .fetch();

        Map<Long, String> map = new LinkedHashMap<>();
        for (Tuple t : rows) {
            map.putIfAbsent(t.get(pgImage.productGroupId), t.get(imageVariant.variantUrl));
        }
        return map;
    }

    // ── Private 헬퍼 ──

    private String findThumbnailUrl(Long productGroupId) {
        return queryFactory
                .select(pgImage.imageUrl)
                .from(pgImage)
                .where(
                        pgImage.productGroupId.eq(productGroupId),
                        pgImage.imageType.eq("THUMBNAIL"),
                        pgImage.deletedAt.isNull())
                .fetchFirst();
    }

    private Map<Long, String> findThumbnailUrls(List<Long> productGroupIds) {
        List<Tuple> thumbnails =
                queryFactory
                        .select(pgImage.productGroupId, pgImage.imageUrl)
                        .from(pgImage)
                        .where(
                                pgImage.productGroupId.in(productGroupIds),
                                pgImage.imageType.eq("THUMBNAIL"),
                                pgImage.deletedAt.isNull())
                        .fetch();

        Map<Long, String> map = new LinkedHashMap<>();
        for (Tuple t : thumbnails) {
            map.putIfAbsent(t.get(pgImage.productGroupId), t.get(pgImage.imageUrl));
        }
        return map;
    }

    private int countProducts(Long productGroupId) {
        Long count =
                queryFactory
                        .select(product.count())
                        .from(product)
                        .where(
                                productConditionBuilder.productGroupIdEq(productGroupId),
                                productConditionBuilder.statusNotDeleted())
                        .fetchOne();
        return count != null ? count.intValue() : 0;
    }

    private Map<Long, Integer> countProductsByGroupIds(List<Long> productGroupIds) {
        List<Tuple> counts =
                queryFactory
                        .select(product.productGroupId, product.count())
                        .from(product)
                        .where(
                                productConditionBuilder.productGroupIdIn(productGroupIds),
                                productConditionBuilder.statusNotDeleted())
                        .groupBy(product.productGroupId)
                        .fetch();

        Map<Long, Integer> map = new LinkedHashMap<>();
        for (Tuple t : counts) {
            Long cnt = t.get(product.count());
            map.put(t.get(product.productGroupId), cnt != null ? cnt.intValue() : 0);
        }
        return map;
    }

    private Map<Long, List<OptionGroupSummaryResult>> findOptionSummariesByProductGroupIds(
            List<Long> productGroupIds) {
        List<Tuple> groups =
                queryFactory
                        .select(
                                optionGroup.id,
                                optionGroup.productGroupId,
                                optionGroup.optionGroupName)
                        .from(optionGroup)
                        .where(
                                optionGroup.productGroupId.in(productGroupIds),
                                optionGroup.deleted.isFalse())
                        .orderBy(optionGroup.sortOrder.asc())
                        .fetch();

        if (groups.isEmpty()) {
            return Map.of();
        }

        List<Long> groupIds = groups.stream().map(g -> g.get(optionGroup.id)).toList();

        List<Tuple> values =
                queryFactory
                        .select(optionValue.sellerOptionGroupId, optionValue.optionValueName)
                        .from(optionValue)
                        .where(
                                optionValue.sellerOptionGroupId.in(groupIds),
                                optionValue.deleted.isFalse())
                        .orderBy(optionValue.sortOrder.asc())
                        .fetch();

        Map<Long, List<String>> valuesByGroupId = new LinkedHashMap<>();
        for (Tuple v : values) {
            valuesByGroupId
                    .computeIfAbsent(v.get(optionValue.sellerOptionGroupId), k -> new ArrayList<>())
                    .add(v.get(optionValue.optionValueName));
        }

        Map<Long, List<OptionGroupSummaryResult>> result = new LinkedHashMap<>();
        for (Tuple g : groups) {
            Long pgId = g.get(optionGroup.productGroupId);
            String groupName = g.get(optionGroup.optionGroupName);
            Long grpId = g.get(optionGroup.id);
            List<String> valueNames = valuesByGroupId.getOrDefault(grpId, List.of());

            result.computeIfAbsent(pgId, k -> new ArrayList<>())
                    .add(new OptionGroupSummaryResult(groupName, valueNames));
        }
        return result;
    }

    private ShippingPolicyResult toShippingPolicyResult(Tuple row) {
        Long id = row.get(shippingPolicy.id);
        if (id == null) {
            return null;
        }
        String feeType = row.get(shippingPolicy.shippingFeeType);
        return new ShippingPolicyResult(
                id,
                row.get(shippingPolicy.policyName),
                Boolean.TRUE.equals(row.get(shippingPolicy.defaultPolicy)),
                Boolean.TRUE.equals(row.get(shippingPolicy.active)),
                feeType,
                feeType,
                toLong(row.get(shippingPolicy.baseFee)),
                toLong(row.get(shippingPolicy.freeThreshold)),
                row.get(shippingPolicy.createdAt));
    }

    private RefundPolicyResult toRefundPolicyResult(Tuple row) {
        Long id = row.get(refundPolicy.id);
        if (id == null) {
            return null;
        }
        return new RefundPolicyResult(
                id,
                row.get(refundPolicy.policyName),
                Boolean.TRUE.equals(row.get(refundPolicy.defaultPolicy)),
                Boolean.TRUE.equals(row.get(refundPolicy.active)),
                safeInt(row.get(refundPolicy.returnPeriodDays)),
                safeInt(row.get(refundPolicy.exchangePeriodDays)),
                List.of(),
                row.get(refundPolicy.createdAt));
    }

    private static int safeInt(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return 0;
    }

    private static Long toLong(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        return null;
    }

    private OrderSpecifier<?> resolveOrderSpecifier(ProductGroupOffsetSearchCriteria criteria) {
        ProductGroupSortKey sortKey = criteria.queryContext().sortKey();
        SortDirection direction = criteria.queryContext().sortDirection();
        boolean isAsc = direction.isAscending();

        return switch (sortKey) {
            case CREATED_AT -> isAsc ? pg.createdAt.asc() : pg.createdAt.desc();
            case UPDATED_AT -> isAsc ? pg.updatedAt.asc() : pg.updatedAt.desc();
            case NAME -> isAsc ? pg.productGroupName.asc() : pg.productGroupName.desc();
            case CURRENT_PRICE -> isAsc ? pg.currentPrice.asc() : pg.currentPrice.desc();
            case SALE_PRICE -> isAsc ? pgPrice.salePrice.asc() : pgPrice.salePrice.desc();
            case DISCOUNT_RATE -> isAsc ? pgPrice.discountRate.asc() : pgPrice.discountRate.desc();
            default -> isAsc ? pg.createdAt.asc() : pg.createdAt.desc();
        };
    }

    /** Tuple → ProductGroupListCompositeResult 변환 헬퍼. */
    private ProductGroupListCompositeResult toListCompositeResult(
            Tuple row, String thumbnailUrl, int productCount) {
        return ProductGroupListCompositeResult.ofBase(
                row.get(pg.id),
                row.get(pg.sellerId),
                row.get(seller.sellerName),
                row.get(pg.brandId),
                row.get(brand.brandName),
                row.get(brand.displayKoreanName),
                row.get(brand.displayEnglishName),
                row.get(brand.brandIconImageUrl),
                row.get(pg.categoryId),
                row.get(category.categoryName),
                row.get(category.path),
                safeInt(row.get(category.categoryDepth)),
                row.get(pg.productGroupName),
                row.get(pg.optionType),
                row.get(pg.status),
                thumbnailUrl,
                productCount,
                safeInt(row.get(pg.regularPrice)),
                safeInt(row.get(pg.currentPrice)),
                safeInt(row.get(pgPrice.salePrice)),
                safeInt(row.get(pgPrice.discountRate)),
                row.get(pg.createdAt),
                row.get(pg.updatedAt));
    }
}
