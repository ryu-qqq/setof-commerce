package com.ryuqq.setof.adapter.in.rest.v1.content.mapper;

import com.ryuqq.setof.adapter.in.rest.common.util.DateTimeFormatUtils;
import com.ryuqq.setof.adapter.in.rest.v1.content.dto.response.ContentMetaV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.content.dto.response.ContentV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.content.dto.response.ContentV1ApiResponse.ComponentDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.content.dto.response.ContentV1ApiResponse.DisplayPeriodResponse;
import com.ryuqq.setof.adapter.in.rest.v1.content.dto.response.OnDisplayContentV1ApiResponse;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.vo.ComponentProductBundle;
import com.ryuqq.setof.domain.contentpage.vo.ComponentSpec;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import com.ryuqq.setof.domain.contentpage.vo.ViewExtension;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * ContentV1ApiMapper - 콘텐츠 도메인 → V1 API 응답 변환.
 *
 * <p>API-MAP-001: @Component 등록, 도메인 → API 응답 변환 전담.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ContentV1ApiMapper {

    public OnDisplayContentV1ApiResponse toOnDisplayResponse(Set<Long> contentIds) {
        return new OnDisplayContentV1ApiResponse(contentIds);
    }

    public ContentMetaV1ApiResponse toMetaResponse(ContentPage page) {
        return new ContentMetaV1ApiResponse(
                page.idValue(),
                new ContentMetaV1ApiResponse.DisplayPeriodResponse(
                        DateTimeFormatUtils.format(page.displayPeriod().startDate()),
                        DateTimeFormatUtils.format(page.displayPeriod().endDate())),
                page.title(),
                page.memo(),
                page.imageUrl(),
                List.of());
    }

    public ContentV1ApiResponse toContentResponse(
            ContentPage page,
            List<DisplayComponent> components,
            ComponentProductBundle productBundle) {
        List<ComponentDetailV1ApiResponse> componentDetails =
                components.stream().map(c -> toComponentDetail(c, productBundle)).toList();

        return new ContentV1ApiResponse(
                page.idValue(),
                new DisplayPeriodResponse(
                        DateTimeFormatUtils.format(page.displayPeriod().startDate()),
                        DateTimeFormatUtils.format(page.displayPeriod().endDate())),
                page.title(),
                page.memo(),
                page.imageUrl(),
                componentDetails);
    }

    private ComponentDetailV1ApiResponse toComponentDetail(
            DisplayComponent component, ComponentProductBundle productBundle) {
        Map<String, Object> inner = buildComponentInner(component, productBundle);

        ViewExtension ve = component.viewExtension();
        Object outerVeDetails = ve != null ? buildViewExtensionMap(ve) : null;

        return new ComponentDetailV1ApiResponse(
                component.idValue(),
                component.name(),
                component.componentType().name(),
                component.displayConfig().listType().name(),
                component.displayConfig().orderType().name(),
                component.displayConfig().badgeType().name(),
                component.displayConfig().filterEnabled() ? "Y" : "N",
                component.isActive() ? "Y" : "N",
                inner,
                outerVeDetails);
    }

    private Map<String, Object> buildComponentInner(
            DisplayComponent component, ComponentProductBundle productBundle) {
        Map<String, Object> inner = new LinkedHashMap<>();

        putCommonHeader(inner, component);

        ComponentSpec spec = component.componentSpec();
        ViewExtension ve = component.viewExtension();

        if (spec instanceof ComponentSpec.TitleSpec title) {
            buildTitleInner(inner, title);
        } else if (spec instanceof ComponentSpec.TextSpec text) {
            buildTextInner(inner, text);
        } else if (spec instanceof ComponentSpec.BlankSpec blank) {
            buildBlankInner(inner, blank);
        } else if (spec instanceof ComponentSpec.ImageSpec image) {
            buildImageInner(inner, image);
        } else if (spec instanceof ComponentSpec.CategorySpec category) {
            buildCategoryInner(inner, category, ve, component, productBundle);
        } else if (spec instanceof ComponentSpec.ProductSpec product) {
            buildProductInner(inner, product, ve, component, productBundle);
        } else if (spec instanceof ComponentSpec.BrandSpec brand) {
            buildBrandInner(inner, brand, ve, component, productBundle);
        } else if (spec instanceof ComponentSpec.TabSpec tab) {
            buildTabInner(inner, tab, ve, component, productBundle);
        }

        return inner;
    }

    private void putCommonHeader(Map<String, Object> inner, DisplayComponent component) {
        inner.put("type", resolveTypeName(component));
        inner.put("componentId", component.idValue());
        inner.put("componentName", component.name());
        Map<String, Object> dp = new LinkedHashMap<>();
        dp.put(
                "displayStartDate",
                DateTimeFormatUtils.format(component.displayPeriod().startDate()));
        dp.put("displayEndDate", DateTimeFormatUtils.format(component.displayPeriod().endDate()));
        inner.put("displayPeriod", dp);
        inner.put("displayOrder", component.displayOrder());
        inner.put("displayYn", component.isActive() ? "Y" : "N");
    }

    // ── TITLE: spec → veDetails → componentType → exposedProducts ──
    private void buildTitleInner(Map<String, Object> inner, ComponentSpec.TitleSpec title) {
        inner.put("titleComponentId", title.titleComponentId());
        inner.put("title1", title.title1());
        inner.put("title2", title.title2());
        inner.put("subTitle1", title.subTitle1());
        inner.put("subTitle2", title.subTitle2());
        inner.put("viewExtensionDetails", null);
        inner.put("componentType", "TITLE");
        inner.put("exposedProducts", 0);
    }

    // ── TEXT: spec → veDetails → componentType → exposedProducts ──
    private void buildTextInner(Map<String, Object> inner, ComponentSpec.TextSpec text) {
        inner.put("textComponentId", text.textComponentId());
        inner.put("content", text.content());
        inner.put("viewExtensionDetails", null);
        inner.put("componentType", "TEXT");
        inner.put("exposedProducts", 0);
    }

    // ── BLANK: spec → veDetails → componentType → exposedProducts ──
    private void buildBlankInner(Map<String, Object> inner, ComponentSpec.BlankSpec blank) {
        inner.put("blankComponentId", blank.blankComponentId());
        inner.put("height", blank.height());
        inner.put("lineYn", blank.showLine() ? "Y" : "N");
        inner.put("viewExtensionDetails", null);
        inner.put("componentType", "BLANK");
        inner.put("exposedProducts", 0);
    }

    // ── IMAGE: spec → imageComponentLinks → veDetails → componentType → exposedProducts ──
    private void buildImageInner(Map<String, Object> inner, ComponentSpec.ImageSpec image) {
        inner.put("imageComponentId", image.imageComponentId());
        inner.put("imageType", image.imageType().name());
        List<Map<String, Object>> slideList =
                image.slides().stream()
                        .map(
                                slide -> {
                                    Map<String, Object> s = new LinkedHashMap<>();
                                    s.put("imageComponentItemId", slide.imageComponentItemId());
                                    s.put("displayOrder", slide.displayOrder());
                                    s.put("imageUrl", slide.imageUrl());
                                    s.put("linkUrl", slide.linkUrl());
                                    return s;
                                })
                        .toList();
        inner.put("imageComponentLinks", slideList);
        inner.put("viewExtensionDetails", null);
        inner.put("componentType", "IMAGE");
        inner.put("exposedProducts", 0);
    }

    // ── CATEGORY: categoryComponentId → categoryId → exposedProducts → veId → veDetails →
    // thumbnails → pageSize → componentType ──
    private void buildCategoryInner(
            Map<String, Object> inner,
            ComponentSpec.CategorySpec category,
            ViewExtension ve,
            DisplayComponent component,
            ComponentProductBundle productBundle) {
        int exposed = category.exposedProducts();
        inner.put("categoryComponentId", category.categoryComponentId());
        inner.put("categoryId", category.categoryId());
        inner.put("exposedProducts", exposed);
        putViewExtensionId(inner, ve);
        putViewExtensionDetails(inner, ve);
        inner.put(
                "productGroupThumbnails",
                toProductThumbnailList(productBundle.forComponent(component.idValue())));
        inner.put("pageSize", exposed);
        inner.put("componentType", "CATEGORY");
    }

    // ── PRODUCT: productComponentId → exposedProducts → veId → veDetails → [thumbnails] → pageSize
    // → componentType ──
    private void buildProductInner(
            Map<String, Object> inner,
            ComponentSpec.ProductSpec product,
            ViewExtension ve,
            DisplayComponent component,
            ComponentProductBundle productBundle) {
        int exposed = product.exposedProducts();
        inner.put("productComponentId", product.productComponentId());
        inner.put("exposedProducts", exposed);
        putViewExtensionId(inner, ve);
        putViewExtensionDetails(inner, ve);
        List<ProductThumbnailSnapshot> thumbnails = productBundle.forComponent(component.idValue());
        if (!thumbnails.isEmpty()) {
            inner.put("productGroupThumbnails", toProductThumbnailList(thumbnails));
        }
        inner.put("pageSize", exposed);
        inner.put("componentType", "PRODUCT");
    }

    // ── BRAND: brandComponentId → brandList → categoryId → exposedProducts → veId → thumbnails →
    // pageSize → componentType ──
    private void buildBrandInner(
            Map<String, Object> inner,
            ComponentSpec.BrandSpec brand,
            ViewExtension ve,
            DisplayComponent component,
            ComponentProductBundle productBundle) {
        int exposed = brand.exposedProducts();
        inner.put("brandComponentId", brand.brandComponentId());
        List<Map<String, Object>> brandList =
                brand.brandFilters().stream()
                        .map(
                                bf -> {
                                    Map<String, Object> m = new LinkedHashMap<>();
                                    m.put("brandId", bf.brandId());
                                    m.put("brandName", bf.brandName());
                                    return m;
                                })
                        .toList();
        inner.put("brandList", brandList);
        inner.put("categoryId", brand.categoryId());
        inner.put("exposedProducts", exposed);
        putViewExtensionId(inner, ve);
        inner.put(
                "productGroupThumbnails",
                toProductThumbnailList(productBundle.forComponent(component.idValue())));
        inner.put("pageSize", exposed);
        inner.put("componentType", "BRAND");
    }

    // ── TAB: tabComponentId → tabDetails → exposedProducts → veId → veDetails → thumbnails →
    // componentType ──
    private void buildTabInner(
            Map<String, Object> inner,
            ComponentSpec.TabSpec tab,
            ViewExtension ve,
            DisplayComponent component,
            ComponentProductBundle productBundle) {
        inner.put("tabComponentId", tab.tabComponentId());

        List<Map<String, Object>> tabDetails =
                tab.tabs().stream()
                        .map(
                                t -> {
                                    Map<String, Object> m = new LinkedHashMap<>();
                                    m.put("tabId", t.tabId());
                                    m.put("tabName", t.tabName());
                                    m.put("stickyYn", tab.sticky() ? "Y" : "N");
                                    m.put("tabMovingType", tab.movingType().name());
                                    m.put("displayPeriod", null);
                                    m.put("displayOrder", t.displayOrder());
                                    m.put(
                                            "productGroupThumbnails",
                                            toProductThumbnailList(
                                                    productBundle.forTab(t.tabId())));
                                    return m;
                                })
                        .toList();
        inner.put("tabDetails", tabDetails);
        inner.put("exposedProducts", tab.exposedProducts());
        putViewExtensionId(inner, ve);
        putViewExtensionDetails(inner, ve);

        // component-level productGroupThumbnails = 전체 탭 상품 합산
        List<ProductThumbnailSnapshot> allTabProducts = new ArrayList<>();
        for (var t : tab.tabs()) {
            allTabProducts.addAll(productBundle.forTab(t.tabId()));
        }
        inner.put("productGroupThumbnails", toProductThumbnailList(allTabProducts));
        inner.put("componentType", "TAB");
    }

    private void putViewExtensionId(Map<String, Object> inner, ViewExtension ve) {
        inner.put("viewExtensionId", ve != null ? ve.viewExtensionId() : null);
    }

    private void putViewExtensionDetails(Map<String, Object> inner, ViewExtension ve) {
        inner.put("viewExtensionDetails", ve != null ? buildViewExtensionMap(ve) : null);
    }

    private Map<String, Object> buildViewExtensionMap(ViewExtension ve) {
        Map<String, Object> veMap = new LinkedHashMap<>();
        veMap.put("viewExtensionType", ve.viewExtensionType().name());
        veMap.put("linkUrl", ve.linkUrl());
        veMap.put("buttonName", ve.buttonName());
        veMap.put("productCountPerClick", ve.productCountPerClick());
        veMap.put("maxClickCount", ve.maxClickCount());
        veMap.put("afterMaxActionType", ve.afterMaxActionType().name());
        veMap.put("afterMaxActionLinkUrl", ve.afterMaxActionLinkUrl());
        return veMap;
    }

    private List<Map<String, Object>> toProductThumbnailList(
            List<ProductThumbnailSnapshot> thumbnails) {
        return thumbnails.stream().map(this::toProductThumbnailMap).toList();
    }

    private Map<String, Object> toProductThumbnailMap(ProductThumbnailSnapshot t) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("productGroupId", t.productGroupId());
        m.put("sellerId", t.sellerId());
        m.put("productGroupName", t.productGroupName());
        Map<String, Object> brandMap = new LinkedHashMap<>();
        brandMap.put("brandId", t.brandId());
        brandMap.put("brandName", t.brandName());
        m.put("brand", brandMap);
        m.put("productImageUrl", t.productImageUrl());
        Map<String, Object> priceMap = new LinkedHashMap<>();
        priceMap.put("regularPrice", t.regularPrice());
        priceMap.put("currentPrice", t.currentPrice());
        priceMap.put("salePrice", t.salePrice());
        priceMap.put("directDiscountRate", t.directDiscountRate());
        priceMap.put("directDiscountPrice", t.directDiscountPrice());
        priceMap.put("discountRate", t.discountRate());
        m.put("price", priceMap);
        m.put(
                "insertDate",
                t.createdAt() != null ? DateTimeFormatUtils.format(t.createdAt()) : null);
        m.put("averageRating", 0.0);
        m.put("reviewCount", 0L);
        m.put("score", 0.0);
        Map<String, Object> statusMap = new LinkedHashMap<>();
        statusMap.put("soldOutYn", t.soldOut() ? "Y" : "N");
        statusMap.put("displayYn", t.displayed() ? "Y" : "N");
        m.put("productStatus", statusMap);
        m.put("favorite", false);
        return m;
    }

    private String resolveTypeName(DisplayComponent component) {
        return switch (component.componentType()) {
            case TITLE -> "titleComponent";
            case TEXT -> "textComponent";
            case BLANK -> "blankComponent";
            case IMAGE -> "imageComponent";
            case PRODUCT -> "productComponent";
            case BRAND -> "brandComponent";
            case CATEGORY -> "categoryComponent";
            case TAB -> "tabComponent";
        };
    }
}
