package com.ryuqq.setof.storage.legacy.composite.content.mapper;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.id.ContentPageId;
import com.ryuqq.setof.domain.contentpage.id.DisplayComponentId;
import com.ryuqq.setof.domain.contentpage.vo.BadgeType;
import com.ryuqq.setof.domain.contentpage.vo.BrandFilter;
import com.ryuqq.setof.domain.contentpage.vo.ComponentSpec;
import com.ryuqq.setof.domain.contentpage.vo.ComponentType;
import com.ryuqq.setof.domain.contentpage.vo.DisplayConfig;
import com.ryuqq.setof.domain.contentpage.vo.DisplayTab;
import com.ryuqq.setof.domain.contentpage.vo.ImageSlide;
import com.ryuqq.setof.domain.contentpage.vo.ImageType;
import com.ryuqq.setof.domain.contentpage.vo.ListType;
import com.ryuqq.setof.domain.contentpage.vo.OrderType;
import com.ryuqq.setof.domain.contentpage.vo.ProductSlot;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import com.ryuqq.setof.domain.contentpage.vo.TabMovingType;
import com.ryuqq.setof.domain.contentpage.vo.ViewExtension;
import com.ryuqq.setof.domain.contentpage.vo.ViewExtensionType;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * LegacyWebContentMapper - 레거시 웹 콘텐츠 QueryDto → 도메인 객체 변환.
 *
 * <p>LocalDateTime → Instant 변환은 이 Mapper에서 담당한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebContentMapper {

    private static final ZoneId LEGACY_ZONE = ZoneId.of("Asia/Seoul");

    /**
     * ContentQueryDto → ContentPage 도메인 변환.
     *
     * @param dto ContentQueryDto
     * @return ContentPage
     */
    public ContentPage toContentPage(LegacyWebContentQueryDto dto) {
        Instant startDate = toInstant(dto.displayStartDate());
        Instant endDate = toInstant(dto.displayEndDate());

        return ContentPage.reconstitute(
                ContentPageId.of(dto.contentId()),
                dto.title(),
                dto.memo(),
                dto.imageUrl(),
                DisplayPeriod.of(startDate, endDate),
                true,
                DeletionStatus.active(),
                startDate,
                startDate);
    }

    /**
     * ComponentQueryDto + ViewExtension + ComponentSpec → DisplayComponent 도메인 변환.
     *
     * @param dto ComponentQueryDto
     * @param viewExtensionMap 뷰 확장 맵
     * @param componentSpecMap 컴포넌트 ID별 ComponentSpec 맵
     * @return DisplayComponent
     */
    public DisplayComponent toDisplayComponent(
            LegacyWebComponentQueryDto dto,
            Map<Long, LegacyWebViewExtensionQueryDto> viewExtensionMap,
            Map<Long, ComponentSpec> componentSpecMap) {
        Instant startDate = toInstant(dto.displayStartDate());
        Instant endDate = toInstant(dto.displayEndDate());

        ViewExtension viewExtension = null;
        if (dto.viewExtensionId() != null && viewExtensionMap.containsKey(dto.viewExtensionId())) {
            viewExtension = toViewExtension(viewExtensionMap.get(dto.viewExtensionId()));
        }

        return DisplayComponent.reconstitute(
                DisplayComponentId.of(dto.componentId()),
                dto.contentId(),
                dto.componentName(),
                dto.displayOrder(),
                parseEnum(ComponentType.class, dto.componentType(), ComponentType.PRODUCT),
                new DisplayConfig(
                        parseEnum(ListType.class, dto.listType(), ListType.NONE),
                        parseEnum(OrderType.class, dto.orderType(), OrderType.NONE),
                        parseEnum(BadgeType.class, dto.badgeType(), BadgeType.NONE),
                        "Y".equals(dto.filterYn())),
                DisplayPeriod.of(startDate, endDate),
                true,
                viewExtension,
                componentSpecMap.get(dto.componentId()),
                DeletionStatus.active(),
                startDate,
                startDate);
    }

    /**
     * ComponentQueryDto 목록 → DisplayComponent 목록 변환.
     *
     * @param dtos ComponentQueryDto 목록
     * @param viewExtensionMap 뷰 확장 맵
     * @return DisplayComponent 목록
     */
    public List<DisplayComponent> toDisplayComponents(
            List<LegacyWebComponentQueryDto> dtos,
            Map<Long, LegacyWebViewExtensionQueryDto> viewExtensionMap,
            Map<Long, ComponentSpec> componentSpecMap) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream()
                .map(dto -> toDisplayComponent(dto, viewExtensionMap, componentSpecMap))
                .collect(Collectors.toList());
    }

    /**
     * ViewExtensionQueryDto → ViewExtension VO 변환.
     *
     * @param dto ViewExtensionQueryDto
     * @return ViewExtension
     */
    public ViewExtension toViewExtension(LegacyWebViewExtensionQueryDto dto) {
        return new ViewExtension(
                dto.viewExtensionId(),
                parseEnum(ViewExtensionType.class, dto.viewExtensionType(), ViewExtensionType.NONE),
                dto.linkUrl(),
                dto.buttonName(),
                dto.productCountPerClick(),
                dto.maxClickCount(),
                parseEnum(
                        ViewExtensionType.class, dto.afterMaxActionType(), ViewExtensionType.NONE),
                dto.afterMaxActionLinkUrl());
    }

    /**
     * ViewExtensionQueryDto 목록 → Map 변환.
     *
     * @param dtos ViewExtensionQueryDto 목록
     * @return Map (viewExtensionId -> ViewExtensionQueryDto)
     */
    public Map<Long, LegacyWebViewExtensionQueryDto> toViewExtensionMap(
            List<LegacyWebViewExtensionQueryDto> dtos) {
        if (dtos == null) {
            return Map.of();
        }
        return dtos.stream()
                .collect(
                        Collectors.toMap(
                                LegacyWebViewExtensionQueryDto::viewExtensionId, dto -> dto));
    }

    /** TitleComponentQueryDto → TitleSpec 변환. */
    public ComponentSpec.TitleSpec toTitleSpec(LegacyWebTitleComponentQueryDto dto) {
        return new ComponentSpec.TitleSpec(
                dto.titleComponentId(),
                dto.title1(),
                dto.title2(),
                dto.subTitle1(),
                dto.subTitle2());
    }

    /** TextComponentQueryDto → TextSpec 변환. */
    public ComponentSpec.TextSpec toTextSpec(LegacyWebTextComponentQueryDto dto) {
        return new ComponentSpec.TextSpec(dto.textComponentId(), dto.content());
    }

    /** BlankComponentQueryDto → BlankSpec 변환. */
    public ComponentSpec.BlankSpec toBlankSpec(LegacyWebBlankComponentQueryDto dto) {
        return new ComponentSpec.BlankSpec(
                dto.blankComponentId(), dto.height(), "Y".equals(dto.lineYn()));
    }

    /** ImageComponentQueryDto + ImageComponentItemQueryDto 목록 → ImageSpec 변환. */
    public ComponentSpec.ImageSpec toImageSpec(
            LegacyWebImageComponentQueryDto dto, List<LegacyWebImageComponentItemQueryDto> items) {
        List<ImageSlide> slides =
                items.stream()
                        .map(
                                item ->
                                        new ImageSlide(
                                                item.imageComponentItemId(),
                                                item.displayOrder(),
                                                item.imageUrl(),
                                                item.linkUrl()))
                        .collect(Collectors.toList());
        return new ComponentSpec.ImageSpec(
                dto.imageComponentId(),
                parseEnum(ImageType.class, dto.imageType(), ImageType.BANNER),
                slides);
    }

    /** CategoryComponentQueryDto → CategorySpec 변환. */
    public ComponentSpec.CategorySpec toCategorySpec(
            LegacyWebCategoryComponentQueryDto dto,
            int exposedProducts,
            List<ProductSlot> fixedProducts,
            List<ProductSlot> autoProducts) {
        return new ComponentSpec.CategorySpec(
                dto.categoryComponentId(),
                dto.categoryId(),
                exposedProducts,
                fixedProducts,
                autoProducts);
    }

    /** ProductComponentQueryDto → ProductSpec 변환. */
    public ComponentSpec.ProductSpec toProductSpec(
            LegacyWebProductComponentQueryDto dto,
            int exposedProducts,
            List<ProductSlot> fixedProducts,
            List<ProductSlot> autoProducts) {
        return new ComponentSpec.ProductSpec(
                dto.productComponentId(), exposedProducts, fixedProducts, autoProducts);
    }

    /** BrandComponentQueryDto + BrandComponentItemQueryDto → BrandSpec 변환. */
    public ComponentSpec.BrandSpec toBrandSpec(
            LegacyWebBrandComponentQueryDto dto,
            int exposedProducts,
            List<LegacyWebBrandComponentItemQueryDto> items,
            List<ProductSlot> fixedProducts,
            List<ProductSlot> autoProducts) {
        List<BrandFilter> brandFilters =
                items.stream()
                        .map(item -> new BrandFilter(item.brandId(), item.brandName()))
                        .collect(Collectors.toList());
        long categoryId = items.isEmpty() ? 0 : items.get(0).categoryId();
        return new ComponentSpec.BrandSpec(
                dto.brandComponentId(),
                categoryId,
                exposedProducts,
                brandFilters,
                fixedProducts,
                autoProducts);
    }

    /** TabComponentQueryDto + TabQueryDto → TabSpec 변환. */
    public ComponentSpec.TabSpec toTabSpec(
            LegacyWebTabComponentQueryDto dto,
            List<LegacyWebTabQueryDto> tabs,
            Map<Long, List<ProductSlot>> fixedProductsByTab,
            Map<Long, List<ProductSlot>> autoProductsByTab,
            int exposedProducts) {
        List<DisplayTab> displayTabs =
                tabs.stream()
                        .map(
                                tab ->
                                        new DisplayTab(
                                                tab.tabId(),
                                                tab.tabName(),
                                                tab.displayOrder(),
                                                fixedProductsByTab.getOrDefault(
                                                        tab.tabId(), List.of()),
                                                autoProductsByTab.getOrDefault(
                                                        tab.tabId(), List.of())))
                        .collect(Collectors.toList());
        return new ComponentSpec.TabSpec(
                dto.tabComponentId(),
                exposedProducts,
                "Y".equals(dto.stickyYn()),
                parseEnum(TabMovingType.class, dto.tabMovingType(), TabMovingType.SCROLL),
                displayTabs);
    }

    /** ComponentProductQueryDto → ProductThumbnailSnapshot 변환. */
    public ProductThumbnailSnapshot toProductThumbnailSnapshot(
            LegacyWebComponentProductQueryDto dto) {
        return new ProductThumbnailSnapshot(
                dto.productGroupId(),
                dto.sellerId(),
                dto.productGroupName(),
                dto.brandId(),
                dto.brandName(),
                dto.productImageUrl(),
                dto.regularPrice(),
                dto.currentPrice(),
                dto.salePrice(),
                dto.directDiscountRate(),
                dto.directDiscountPrice(),
                dto.discountRate(),
                dto.insertDate(),
                dto.averageRating(),
                dto.reviewCount(),
                dto.score(),
                false,
                dto.displayYn(),
                dto.soldOutYn());
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return Instant.now();
        }
        return localDateTime.atZone(LEGACY_ZONE).toInstant();
    }

    private <E extends Enum<E>> E parseEnum(Class<E> enumClass, String value, E defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }
}
