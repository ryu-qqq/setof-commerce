package com.ryuqq.setof.adapter.out.persistence.composite.content.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.brand.entity.QBrandJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.composite.content.condition.ContentCompositeConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.composite.content.dto.AutoProductThumbnailDto;
import com.ryuqq.setof.adapter.out.persistence.composite.content.dto.FixedProductThumbnailDto;
import com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.QComponentFixedProductJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QProductGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupimage.entity.QProductGroupImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupprice.entity.QProductGroupPriceJpaEntity;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * ContentCompositeQueryDslRepository - 콘텐츠 상품 크로스 도메인 JOIN 조회용.
 *
 * <p>component_fixed_product + product_groups + brand + product_group_images를 JOIN으로 조회한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class ContentCompositeQueryDslRepository {

    private static final QComponentFixedProductJpaEntity cfp =
            QComponentFixedProductJpaEntity.componentFixedProductJpaEntity;
    private static final QProductGroupJpaEntity pg = QProductGroupJpaEntity.productGroupJpaEntity;
    private static final QBrandJpaEntity brand = QBrandJpaEntity.brandJpaEntity;
    private static final QProductGroupImageJpaEntity pgImage =
            QProductGroupImageJpaEntity.productGroupImageJpaEntity;
    private static final QProductGroupPriceJpaEntity pgPrice =
            QProductGroupPriceJpaEntity.productGroupPriceJpaEntity;

    private final JPAQueryFactory queryFactory;
    private final ContentCompositeConditionBuilder conditionBuilder;

    public ContentCompositeQueryDslRepository(
            JPAQueryFactory queryFactory, ContentCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * FIXED 상품 JOIN 조회.
     *
     * <p>component_fixed_product LEFT JOIN product_groups LEFT JOIN brand LEFT JOIN
     * product_group_prices.
     *
     * @param componentIds 컴포넌트 ID 목록
     * @return FIXED 상품 DTO 목록 (displayOrder ASC)
     */
    public List<FixedProductThumbnailDto> fetchFixedProductThumbnails(List<Long> componentIds) {
        if (componentIds == null || componentIds.isEmpty()) {
            return List.of();
        }

        List<Tuple> rows =
                queryFactory
                        .select(
                                cfp.componentId,
                                cfp.tabId,
                                cfp.productGroupId,
                                cfp.displayName,
                                cfp.displayImageUrl,
                                pg.sellerId,
                                pg.productGroupName,
                                pg.brandId,
                                brand.brandName,
                                pg.regularPrice,
                                pg.currentPrice,
                                pgPrice.salePrice,
                                pg.createdAt)
                        .from(cfp)
                        .leftJoin(pg)
                        .on(cfp.productGroupId.eq(pg.id))
                        .leftJoin(brand)
                        .on(pg.brandId.eq(brand.id))
                        .leftJoin(pgPrice)
                        .on(pgPrice.productGroupId.eq(pg.id))
                        .where(
                                conditionBuilder.componentIdIn(componentIds),
                                conditionBuilder.fixedProductNotDeleted())
                        .orderBy(cfp.displayOrder.asc())
                        .fetch();

        if (rows.isEmpty()) {
            return List.of();
        }

        List<Long> productGroupIds =
                rows.stream().map(r -> r.get(cfp.productGroupId)).distinct().toList();
        Map<Long, String> imageMap = findThumbnailUrls(productGroupIds);

        return rows.stream()
                .filter(r -> r.get(pg.sellerId) != null)
                .map(r -> toFixedDto(r, imageMap))
                .toList();
    }

    /**
     * AUTO 상품 JOIN 조회.
     *
     * <p>product_groups LEFT JOIN brand LEFT JOIN product_group_prices. categoryIds/brandIds 기반 동적
     * 필터링.
     *
     * @param categoryIds 카테고리 ID 목록 (빈 리스트이면 미지정)
     * @param brandIds 브랜드 ID 목록 (빈 리스트이면 미지정)
     * @param limit 최대 조회 수
     * @return AUTO 상품 DTO 목록 (id DESC)
     */
    public List<AutoProductThumbnailDto> fetchAutoProductThumbnails(
            List<Long> categoryIds, List<Long> brandIds, int limit) {

        List<Tuple> rows =
                queryFactory
                        .select(
                                pg.id,
                                pg.sellerId,
                                pg.productGroupName,
                                pg.brandId,
                                brand.brandName,
                                pg.regularPrice,
                                pg.currentPrice,
                                pgPrice.salePrice,
                                pg.createdAt)
                        .from(pg)
                        .leftJoin(brand)
                        .on(pg.brandId.eq(brand.id))
                        .leftJoin(pgPrice)
                        .on(pgPrice.productGroupId.eq(pg.id))
                        .where(
                                conditionBuilder.categoryIdIn(categoryIds),
                                conditionBuilder.brandIdIn(brandIds),
                                conditionBuilder.productGroupStatusNotDeleted())
                        .orderBy(pg.id.desc())
                        .limit(limit)
                        .fetch();

        if (rows.isEmpty()) {
            return List.of();
        }

        List<Long> productGroupIds = rows.stream().map(r -> r.get(pg.id)).distinct().toList();
        Map<Long, String> imageMap = findThumbnailUrls(productGroupIds);

        return rows.stream().map(r -> toAutoDto(r, imageMap)).toList();
    }

    // ── Private 헬퍼 ──

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

    private FixedProductThumbnailDto toFixedDto(Tuple r, Map<Long, String> imageMap) {
        long pgId = r.get(cfp.productGroupId);
        return new FixedProductThumbnailDto(
                r.get(cfp.componentId),
                r.get(cfp.tabId),
                pgId,
                r.get(cfp.displayName),
                r.get(cfp.displayImageUrl),
                safeLong(r.get(pg.sellerId)),
                r.get(pg.productGroupName),
                safeLong(r.get(pg.brandId)),
                r.get(brand.brandName) != null ? r.get(brand.brandName) : "",
                safeInt(r.get(pg.regularPrice)),
                safeInt(r.get(pg.currentPrice)),
                safeInt(r.get(pgPrice.salePrice)),
                r.get(pg.createdAt),
                imageMap.getOrDefault(pgId, ""));
    }

    private AutoProductThumbnailDto toAutoDto(Tuple r, Map<Long, String> imageMap) {
        long pgId = r.get(pg.id);
        return new AutoProductThumbnailDto(
                pgId,
                safeLong(r.get(pg.sellerId)),
                r.get(pg.productGroupName),
                safeLong(r.get(pg.brandId)),
                r.get(brand.brandName) != null ? r.get(brand.brandName) : "",
                safeInt(r.get(pg.regularPrice)),
                safeInt(r.get(pg.currentPrice)),
                safeInt(r.get(pgPrice.salePrice)),
                r.get(pg.createdAt),
                imageMap.getOrDefault(pgId, ""));
    }

    private static int safeInt(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return 0;
    }

    private static long safeLong(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        return 0L;
    }
}
