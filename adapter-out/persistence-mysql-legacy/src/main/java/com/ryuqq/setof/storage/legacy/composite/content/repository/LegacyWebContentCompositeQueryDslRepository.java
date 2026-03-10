package com.ryuqq.setof.storage.legacy.composite.content.repository;

import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyBlankComponentEntity.legacyBlankComponentEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyBrandComponentEntity.legacyBrandComponentEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyBrandComponentItemEntity.legacyBrandComponentItemEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyCategoryComponentEntity.legacyCategoryComponentEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyComponentEntity.legacyComponentEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyComponentItemEntity.legacyComponentItemEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyComponentTargetEntity.legacyComponentTargetEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyContentEntity.legacyContentEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyImageComponentEntity.legacyImageComponentEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyImageComponentItemEntity.legacyImageComponentItemEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyProductComponentEntity.legacyProductComponentEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyTabComponentEntity.legacyTabComponentEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyTabEntity.legacyTabEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyTextComponentEntity.legacyTextComponentEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyTitleComponentEntity.legacyTitleComponentEntity;
import static com.ryuqq.setof.storage.legacy.content.entity.QLegacyViewExtensionEntity.legacyViewExtensionEntity;
import static com.ryuqq.setof.storage.legacy.productgroup.entity.QLegacyProductGroupEntity.legacyProductGroupEntity;
import static com.ryuqq.setof.storage.legacy.productgroup.entity.QLegacyProductGroupImageEntity.legacyProductGroupImageEntity;
import static com.ryuqq.setof.storage.legacy.productgroup.entity.QLegacyProductScoreEntity.legacyProductScoreEntity;
import static com.ryuqq.setof.storage.legacy.review.entity.QLegacyProductRatingStatsEntity.legacyProductRatingStatsEntity;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.content.dto.query.LegacyContentSearchCondition;
import com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity;
import com.ryuqq.setof.storage.legacy.composite.content.condition.LegacyWebContentCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebBlankComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebBrandComponentItemQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebBrandComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebCategoryComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebComponentProductQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebContentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebImageComponentItemQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebImageComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebProductComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebTabComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebTabQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebTextComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebTitleComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebViewExtensionQueryDto;
import com.ryuqq.setof.storage.legacy.productgroup.entity.LegacyProductGroupEntity;
import com.ryuqq.setof.storage.legacy.productgroup.entity.LegacyProductGroupImageEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebContentCompositeQueryDslRepository - 레거시 웹 콘텐츠 Composite 조회 Repository.
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
public class LegacyWebContentCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebContentCompositeConditionBuilder conditionBuilder;

    public LegacyWebContentCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebContentCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 전시 중인 콘텐츠 ID 목록 조회.
     *
     * <p>Content + Component 조인하여 현재 전시 중인 콘텐츠 ID를 조회.
     *
     * @return 전시 중인 콘텐츠 ID 목록
     */
    public List<Long> fetchOnDisplayContentIds() {
        return queryFactory
                .select(legacyContentEntity.id)
                .from(legacyContentEntity)
                .innerJoin(legacyComponentEntity)
                .on(legacyComponentEntity.contentId.eq(legacyContentEntity.id))
                .where(
                        conditionBuilder.onDisplayContent(false),
                        conditionBuilder.onDisplayComponent(false))
                .distinct()
                .fetch();
    }

    /**
     * 콘텐츠 단건 조회 (메타데이터만).
     *
     * @param contentId 콘텐츠 ID
     * @return 콘텐츠 Optional
     */
    public Optional<LegacyWebContentQueryDto> fetchContentById(Long contentId) {
        LegacyWebContentQueryDto dto =
                queryFactory
                        .select(createContentProjection())
                        .from(legacyContentEntity)
                        .where(conditionBuilder.contentIdEq(contentId))
                        .fetchOne();
        return Optional.ofNullable(dto);
    }

    /**
     * 콘텐츠 조회 (삭제되지 않은 것만).
     *
     * @param contentId 콘텐츠 ID
     * @return 콘텐츠 Optional
     */
    public Optional<LegacyWebContentQueryDto> fetchContent(Long contentId) {
        LegacyWebContentQueryDto dto =
                queryFactory
                        .select(createContentProjection())
                        .from(legacyContentEntity)
                        .where(
                                conditionBuilder.contentIdEq(contentId),
                                conditionBuilder.contentNotDeleted())
                        .fetchOne();
        return Optional.ofNullable(dto);
    }

    /**
     * 콘텐츠에 속한 컴포넌트 목록 조회.
     *
     * @param condition 검색 조건
     * @return 컴포넌트 목록
     */
    public List<LegacyWebComponentQueryDto> fetchComponentsByContentId(
            LegacyContentSearchCondition condition) {
        return queryFactory
                .select(createComponentProjection())
                .from(legacyComponentEntity)
                .where(
                        conditionBuilder.componentContentIdEq(condition.contentId()),
                        conditionBuilder.onDisplayComponent(condition.bypass()))
                .orderBy(legacyComponentEntity.displayOrder.asc(), legacyComponentEntity.id.asc())
                .fetch();
    }

    /**
     * 뷰 확장 ID 목록으로 조회.
     *
     * @param viewExtensionIds 뷰 확장 ID 목록
     * @return 뷰 확장 목록
     */
    public List<LegacyWebViewExtensionQueryDto> fetchViewExtensionsByIds(
            List<Long> viewExtensionIds) {
        if (viewExtensionIds == null || viewExtensionIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .select(createViewExtensionProjection())
                .from(legacyViewExtensionEntity)
                .where(legacyViewExtensionEntity.id.in(viewExtensionIds))
                .fetch();
    }

    /**
     * 콘텐츠와 컴포넌트, 뷰 확장 조인 조회.
     *
     * <p>fetchContent 엔드포인트용 복합 쿼리.
     *
     * @param condition 검색 조건
     * @return 컴포넌트 목록 (뷰 확장 ID 포함)
     */
    public List<LegacyWebComponentQueryDto> fetchContentWithComponents(
            LegacyContentSearchCondition condition) {
        return queryFactory
                .select(createComponentProjection())
                .from(legacyContentEntity)
                .leftJoin(legacyComponentEntity)
                .on(legacyComponentEntity.contentId.eq(legacyContentEntity.id))
                .where(
                        conditionBuilder.contentIdEq(condition.contentId()),
                        conditionBuilder.onDisplayContent(condition.bypass()),
                        conditionBuilder.onDisplayComponent(condition.bypass()))
                .orderBy(legacyComponentEntity.displayOrder.asc())
                .fetch();
    }

    /**
     * 타이틀 컴포넌트 상세 조회.
     *
     * @param componentIds 컴포넌트 ID 목록
     * @return 타이틀 컴포넌트 목록
     */
    public List<LegacyWebTitleComponentQueryDto> fetchTitleComponents(List<Long> componentIds) {
        if (componentIds == null || componentIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebTitleComponentQueryDto.class,
                                legacyTitleComponentEntity.id,
                                legacyTitleComponentEntity.componentId,
                                legacyTitleComponentEntity.title1,
                                legacyTitleComponentEntity.title2,
                                legacyTitleComponentEntity.subTitle1,
                                legacyTitleComponentEntity.subTitle2))
                .from(legacyTitleComponentEntity)
                .where(
                        legacyTitleComponentEntity.componentId.in(componentIds),
                        legacyTitleComponentEntity.deleteYn.eq("N"))
                .fetch();
    }

    /**
     * 텍스트 컴포넌트 상세 조회.
     *
     * @param componentIds 컴포넌트 ID 목록
     * @return 텍스트 컴포넌트 목록
     */
    public List<LegacyWebTextComponentQueryDto> fetchTextComponents(List<Long> componentIds) {
        if (componentIds == null || componentIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebTextComponentQueryDto.class,
                                legacyTextComponentEntity.id,
                                legacyTextComponentEntity.componentId,
                                legacyTextComponentEntity.content))
                .from(legacyTextComponentEntity)
                .where(
                        legacyTextComponentEntity.componentId.in(componentIds),
                        legacyTextComponentEntity.deleteYn.eq("N"))
                .fetch();
    }

    /**
     * 여백 컴포넌트 상세 조회.
     *
     * @param componentIds 컴포넌트 ID 목록
     * @return 여백 컴포넌트 목록
     */
    public List<LegacyWebBlankComponentQueryDto> fetchBlankComponents(List<Long> componentIds) {
        if (componentIds == null || componentIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebBlankComponentQueryDto.class,
                                legacyBlankComponentEntity.id,
                                legacyBlankComponentEntity.componentId,
                                legacyBlankComponentEntity.height,
                                legacyBlankComponentEntity.lineYn))
                .from(legacyBlankComponentEntity)
                .where(
                        legacyBlankComponentEntity.componentId.in(componentIds),
                        legacyBlankComponentEntity.deleteYn.eq("N"))
                .fetch();
    }

    /**
     * 이미지 컴포넌트 상세 조회.
     *
     * @param componentIds 컴포넌트 ID 목록
     * @return 이미지 컴포넌트 목록
     */
    public List<LegacyWebImageComponentQueryDto> fetchImageComponents(List<Long> componentIds) {
        if (componentIds == null || componentIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebImageComponentQueryDto.class,
                                legacyImageComponentEntity.id,
                                legacyImageComponentEntity.componentId,
                                legacyImageComponentEntity.imageType))
                .from(legacyImageComponentEntity)
                .where(
                        legacyImageComponentEntity.componentId.in(componentIds),
                        legacyImageComponentEntity.deleteYn.eq("N"))
                .fetch();
    }

    /**
     * 이미지 컴포넌트 아이템 조회.
     *
     * @param imageComponentIds 이미지 컴포넌트 ID 목록
     * @return 이미지 컴포넌트 아이템 목록
     */
    public List<LegacyWebImageComponentItemQueryDto> fetchImageComponentItems(
            List<Long> imageComponentIds) {
        if (imageComponentIds == null || imageComponentIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebImageComponentItemQueryDto.class,
                                legacyImageComponentItemEntity.id,
                                legacyImageComponentItemEntity.imageComponentId,
                                legacyImageComponentItemEntity.imageUrl,
                                legacyImageComponentItemEntity.displayOrder,
                                legacyImageComponentItemEntity.linkUrl,
                                legacyImageComponentItemEntity.width,
                                legacyImageComponentItemEntity.height))
                .from(legacyImageComponentItemEntity)
                .where(
                        legacyImageComponentItemEntity.imageComponentId.in(imageComponentIds),
                        legacyImageComponentItemEntity.deleteYn.eq("N"))
                .orderBy(legacyImageComponentItemEntity.displayOrder.asc())
                .fetch();
    }

    /**
     * 카테고리 컴포넌트 상세 조회.
     *
     * @param componentIds 컴포넌트 ID 목록
     * @return 카테고리 컴포넌트 목록
     */
    public List<LegacyWebCategoryComponentQueryDto> fetchCategoryComponents(
            List<Long> componentIds) {
        if (componentIds == null || componentIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebCategoryComponentQueryDto.class,
                                legacyCategoryComponentEntity.id,
                                legacyCategoryComponentEntity.componentId,
                                legacyCategoryComponentEntity.categoryId))
                .from(legacyCategoryComponentEntity)
                .where(legacyCategoryComponentEntity.componentId.in(componentIds))
                .fetch();
    }

    /**
     * 상품 컴포넌트 상세 조회.
     *
     * @param componentIds 컴포넌트 ID 목록
     * @return 상품 컴포넌트 목록
     */
    public List<LegacyWebProductComponentQueryDto> fetchProductComponents(List<Long> componentIds) {
        if (componentIds == null || componentIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebProductComponentQueryDto.class,
                                legacyProductComponentEntity.id,
                                legacyProductComponentEntity.componentId))
                .from(legacyProductComponentEntity)
                .where(legacyProductComponentEntity.componentId.in(componentIds))
                .fetch();
    }

    /**
     * 브랜드 컴포넌트 상세 조회.
     *
     * @param componentIds 컴포넌트 ID 목록
     * @return 브랜드 컴포넌트 목록
     */
    public List<LegacyWebBrandComponentQueryDto> fetchBrandComponents(List<Long> componentIds) {
        if (componentIds == null || componentIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebBrandComponentQueryDto.class,
                                legacyBrandComponentEntity.id,
                                legacyBrandComponentEntity.componentId))
                .from(legacyBrandComponentEntity)
                .where(legacyBrandComponentEntity.componentId.in(componentIds))
                .fetch();
    }

    /**
     * 브랜드 컴포넌트 아이템 조회.
     *
     * @param brandComponentIds 브랜드 컴포넌트 ID 목록
     * @return 브랜드 컴포넌트 아이템 목록
     */
    public List<LegacyWebBrandComponentItemQueryDto> fetchBrandComponentItems(
            List<Long> brandComponentIds) {
        if (brandComponentIds == null || brandComponentIds.isEmpty()) {
            return List.of();
        }
        QLegacyBrandEntity brand = QLegacyBrandEntity.legacyBrandEntity;
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebBrandComponentItemQueryDto.class,
                                legacyBrandComponentItemEntity.id,
                                legacyBrandComponentItemEntity.brandComponentId,
                                legacyBrandComponentItemEntity.brandId,
                                legacyBrandComponentItemEntity.categoryId,
                                brand.brandName))
                .from(legacyBrandComponentItemEntity)
                .leftJoin(brand)
                .on(legacyBrandComponentItemEntity.brandId.eq(brand.id))
                .where(
                        legacyBrandComponentItemEntity.brandComponentId.in(brandComponentIds),
                        legacyBrandComponentItemEntity.deleteYn.eq("N"))
                .fetch();
    }

    /**
     * 탭 컴포넌트 상세 조회.
     *
     * @param componentIds 컴포넌트 ID 목록
     * @return 탭 컴포넌트 목록
     */
    public List<LegacyWebTabComponentQueryDto> fetchTabComponents(List<Long> componentIds) {
        if (componentIds == null || componentIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebTabComponentQueryDto.class,
                                legacyTabComponentEntity.id,
                                legacyTabComponentEntity.componentId,
                                legacyTabComponentEntity.stickyYn,
                                legacyTabComponentEntity.tabMovingType))
                .from(legacyTabComponentEntity)
                .where(legacyTabComponentEntity.componentId.in(componentIds))
                .fetch();
    }

    /**
     * 탭 목록 조회.
     *
     * @param tabComponentIds 탭 컴포넌트 ID 목록
     * @return 탭 목록
     */
    public List<LegacyWebTabQueryDto> fetchTabs(List<Long> tabComponentIds) {
        if (tabComponentIds == null || tabComponentIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebTabQueryDto.class,
                                legacyTabEntity.id,
                                legacyTabEntity.tabName,
                                legacyTabEntity.tabComponentId,
                                legacyTabEntity.displayOrder))
                .from(legacyTabEntity)
                .where(
                        legacyTabEntity.tabComponentId.in(tabComponentIds),
                        legacyTabEntity.deleteYn.eq("N"))
                .orderBy(legacyTabEntity.displayOrder.asc())
                .fetch();
    }

    /**
     * 컴포넌트 상품 썸네일 조회.
     *
     * <p>component → component_target → component_item → product_group + brand + image +
     * rating_stats + score 조인.
     *
     * @param componentIds 컴포넌트 ID 목록
     * @return 컴포넌트 상품 목록
     */
    public List<LegacyWebComponentProductQueryDto> fetchComponentProducts(List<Long> componentIds) {
        if (componentIds == null || componentIds.isEmpty()) {
            return List.of();
        }
        QLegacyBrandEntity brand = QLegacyBrandEntity.legacyBrandEntity;

        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebComponentProductQueryDto.class,
                                legacyComponentTargetEntity.componentId,
                                legacyComponentTargetEntity.tabId,
                                legacyComponentTargetEntity.sortType,
                                legacyProductGroupEntity.id,
                                legacyProductGroupEntity.sellerId,
                                legacyComponentItemEntity.productDisplayName.coalesce(
                                        legacyProductGroupEntity.productGroupName),
                                brand.id,
                                brand.brandName,
                                legacyComponentItemEntity.productDisplayImage.coalesce(
                                        legacyProductGroupImageEntity.imageUrl),
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
                                legacyProductGroupEntity.soldOutYn.stringValue(),
                                legacyComponentItemEntity.displayOrder))
                .from(legacyComponentTargetEntity)
                .innerJoin(legacyComponentItemEntity)
                .on(legacyComponentItemEntity.componentTargetId.eq(legacyComponentTargetEntity.id))
                .innerJoin(legacyProductGroupEntity)
                .on(
                        legacyProductGroupEntity.id.eq(legacyComponentItemEntity.productGroupId),
                        legacyProductGroupEntity.displayYn.eq(LegacyProductGroupEntity.Yn.Y),
                        legacyProductGroupEntity.soldOutYn.eq(LegacyProductGroupEntity.Yn.N))
                .innerJoin(legacyProductGroupImageEntity)
                .on(
                        legacyProductGroupImageEntity
                                .productGroupId
                                .eq(legacyProductGroupEntity.id)
                                .and(
                                        legacyProductGroupImageEntity.productGroupImageType.eq(
                                                LegacyProductGroupImageEntity.ProductGroupImageType
                                                        .MAIN))
                                .and(
                                        legacyProductGroupImageEntity.deleteYn.eq(
                                                LegacyProductGroupImageEntity.Yn.N)))
                .innerJoin(brand)
                .on(brand.id.eq(legacyProductGroupEntity.brandId))
                .leftJoin(legacyProductRatingStatsEntity)
                .on(legacyProductRatingStatsEntity.id.eq(legacyProductGroupEntity.id))
                .leftJoin(legacyProductScoreEntity)
                .on(legacyProductScoreEntity.id.eq(legacyProductGroupEntity.id))
                .where(
                        legacyComponentTargetEntity.componentId.in(componentIds),
                        legacyComponentTargetEntity.deleteYn.eq("N"))
                .orderBy(legacyComponentItemEntity.displayOrder.asc())
                .fetch();
    }

    /**
     * AUTO 상품 조회 (카테고리/브랜드 기반).
     *
     * <p>product_group 테이블에서 categoryId, brandIds 조건으로 직접 조회한다. displayYn=Y, soldOutYn=N 필터 적용.
     *
     * @param categoryId 카테고리 ID (0이면 미적용)
     * @param brandIds 브랜드 ID 목록 (빈 리스트이면 미적용)
     * @param limit 최대 조회 수
     * @return 상품 목록
     */
    public List<LegacyWebComponentProductQueryDto> fetchAutoProducts(
            long categoryId, List<Long> brandIds, int limit) {
        QLegacyBrandEntity brand = QLegacyBrandEntity.legacyBrandEntity;

        BooleanExpression categoryCondition =
                categoryId != 0L ? legacyProductGroupEntity.categoryId.eq(categoryId) : null;
        BooleanExpression brandCondition =
                (brandIds != null && !brandIds.isEmpty())
                        ? legacyProductGroupEntity.brandId.in(brandIds)
                        : null;

        var query =
                queryFactory
                        .select(
                                Projections.constructor(
                                        LegacyWebComponentProductQueryDto.class,
                                        Expressions.constant(0L),
                                        Expressions.constant(0L),
                                        Expressions.constant("AUTO"),
                                        legacyProductGroupEntity.id,
                                        legacyProductGroupEntity.sellerId,
                                        legacyProductGroupEntity.productGroupName,
                                        brand.id,
                                        brand.brandName,
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
                                        legacyProductGroupEntity.soldOutYn.stringValue(),
                                        Expressions.nullExpression(Long.class)))
                        .from(legacyProductGroupEntity)
                        .innerJoin(legacyProductGroupImageEntity)
                        .on(
                                legacyProductGroupImageEntity
                                        .productGroupId
                                        .eq(legacyProductGroupEntity.id)
                                        .and(
                                                legacyProductGroupImageEntity.productGroupImageType
                                                        .eq(
                                                                LegacyProductGroupImageEntity
                                                                        .ProductGroupImageType
                                                                        .MAIN))
                                        .and(
                                                legacyProductGroupImageEntity.deleteYn.eq(
                                                        LegacyProductGroupImageEntity.Yn.N)))
                        .innerJoin(brand)
                        .on(brand.id.eq(legacyProductGroupEntity.brandId))
                        .leftJoin(legacyProductRatingStatsEntity)
                        .on(legacyProductRatingStatsEntity.id.eq(legacyProductGroupEntity.id))
                        .leftJoin(legacyProductScoreEntity)
                        .on(legacyProductScoreEntity.id.eq(legacyProductGroupEntity.id))
                        .where(
                                legacyProductGroupEntity.displayYn.eq(
                                        LegacyProductGroupEntity.Yn.Y),
                                legacyProductGroupEntity.soldOutYn.eq(
                                        LegacyProductGroupEntity.Yn.N),
                                categoryCondition,
                                brandCondition);

        if (limit < Integer.MAX_VALUE) {
            query.limit(limit);
        }

        return query.fetch();
    }

    /** Projections.constructor()로 Content Projection 생성. */
    private ConstructorExpression<LegacyWebContentQueryDto> createContentProjection() {
        return Projections.constructor(
                LegacyWebContentQueryDto.class,
                legacyContentEntity.id,
                legacyContentEntity.title,
                legacyContentEntity.memo,
                legacyContentEntity.imageUrl,
                legacyContentEntity.displayStartDate,
                legacyContentEntity.displayEndDate);
    }

    /** Projections.constructor()로 Component Projection 생성. */
    private ConstructorExpression<LegacyWebComponentQueryDto> createComponentProjection() {
        return Projections.constructor(
                LegacyWebComponentQueryDto.class,
                legacyComponentEntity.id,
                legacyComponentEntity.contentId,
                legacyComponentEntity.componentName,
                legacyComponentEntity.componentType.stringValue(),
                legacyComponentEntity.listType.stringValue(),
                legacyComponentEntity.orderType.stringValue(),
                legacyComponentEntity.badgeType.stringValue(),
                legacyComponentEntity.filterYn.stringValue(),
                legacyComponentEntity.exposedProducts,
                legacyComponentEntity.displayOrder,
                legacyComponentEntity.displayStartDate,
                legacyComponentEntity.displayEndDate,
                legacyComponentEntity.viewExtensionId);
    }

    /** Projections.constructor()로 ViewExtension Projection 생성. */
    private ConstructorExpression<LegacyWebViewExtensionQueryDto> createViewExtensionProjection() {
        return Projections.constructor(
                LegacyWebViewExtensionQueryDto.class,
                legacyViewExtensionEntity.id,
                legacyViewExtensionEntity.viewExtensionType.stringValue(),
                legacyViewExtensionEntity.linkUrl,
                legacyViewExtensionEntity.buttonName,
                legacyViewExtensionEntity.productCountPerClick,
                legacyViewExtensionEntity.maxClickCount,
                legacyViewExtensionEntity.afterMaxActionType.stringValue(),
                legacyViewExtensionEntity.afterMaxActionLinkUrl);
    }
}
