package com.ryuqq.setof.adapter.out.persistence.displaycomponent.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.DisplayComponentJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.DisplayTabJpaEntity;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
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
import com.ryuqq.setof.domain.contentpage.vo.TabMovingType;
import com.ryuqq.setof.domain.contentpage.vo.ViewExtension;
import com.ryuqq.setof.domain.contentpage.vo.ViewExtensionType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class DisplayComponentJpaEntityMapper {

    private final ObjectMapper objectMapper;

    public DisplayComponentJpaEntityMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public DisplayComponent toDomain(
            DisplayComponentJpaEntity entity, List<DisplayTabJpaEntity> tabs) {
        ComponentType componentType = ComponentType.valueOf(entity.getComponentType());

        DisplayConfig displayConfig =
                new DisplayConfig(
                        parseEnum(ListType.class, entity.getListType(), ListType.NONE),
                        parseEnum(OrderType.class, entity.getOrderType(), OrderType.NONE),
                        parseEnum(BadgeType.class, entity.getBadgeType(), BadgeType.NONE),
                        entity.isFilterEnabled());

        DisplayPeriod displayPeriod =
                DisplayPeriod.of(entity.getDisplayStartAt(), entity.getDisplayEndAt());

        ViewExtension viewExtension = buildViewExtension(entity);

        ComponentSpec componentSpec = buildComponentSpec(entity, componentType, tabs);

        DeletionStatus deletionStatus =
                entity.getDeletedAt() != null
                        ? DeletionStatus.deletedAt(entity.getDeletedAt())
                        : DeletionStatus.active();

        return DisplayComponent.reconstitute(
                DisplayComponentId.of(entity.getId()),
                entity.getContentPageId(),
                entity.getName(),
                entity.getDisplayOrder(),
                componentType,
                displayConfig,
                displayPeriod,
                entity.isActive(),
                viewExtension,
                componentSpec,
                deletionStatus,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public DisplayComponentJpaEntity toEntity(DisplayComponent domain) {
        DisplayConfig config = domain.displayConfig();
        ViewExtension ve = domain.viewExtension();

        String specData = serializeComponentSpec(domain.componentSpec());

        return DisplayComponentJpaEntity.create(
                domain.idValue(),
                domain.contentPageId(),
                domain.name(),
                domain.componentType().name(),
                domain.displayOrder(),
                config.listType().name(),
                config.orderType().name(),
                config.badgeType().name(),
                config.filterEnabled(),
                resolveExposedProducts(domain.componentSpec()),
                domain.displayPeriod().startDate(),
                domain.displayPeriod().endDate(),
                domain.isActive(),
                ve != null ? ve.viewExtensionType().name() : null,
                ve != null ? ve.linkUrl() : null,
                ve != null ? ve.buttonName() : null,
                ve != null ? ve.productCountPerClick() : null,
                ve != null ? ve.maxClickCount() : null,
                ve != null && ve.afterMaxActionType() != null
                        ? ve.afterMaxActionType().name()
                        : null,
                ve != null ? ve.afterMaxActionLinkUrl() : null,
                specData,
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletionStatus().deletedAt());
    }

    // ── Private helpers ──

    private ViewExtension buildViewExtension(DisplayComponentJpaEntity entity) {
        if (entity.getViewExtensionType() == null) {
            return null;
        }
        return new ViewExtension(
                entity.getId(),
                parseEnum(
                        ViewExtensionType.class,
                        entity.getViewExtensionType(),
                        ViewExtensionType.NONE),
                entity.getViewExtensionLinkUrl(),
                entity.getViewExtensionButtonName(),
                entity.getViewExtensionProductCountPerClick() != null
                        ? entity.getViewExtensionProductCountPerClick()
                        : 0,
                entity.getViewExtensionMaxClickCount() != null
                        ? entity.getViewExtensionMaxClickCount()
                        : 0,
                parseEnum(
                        ViewExtensionType.class,
                        entity.getViewExtensionAfterMaxActionType(),
                        ViewExtensionType.NONE),
                entity.getViewExtensionAfterMaxActionLinkUrl());
    }

    private ComponentSpec buildComponentSpec(
            DisplayComponentJpaEntity entity,
            ComponentType componentType,
            List<DisplayTabJpaEntity> tabs) {
        String specData = entity.getSpecData();
        long componentId = entity.getId();

        try {
            return switch (componentType) {
                case TEXT -> buildTextSpec(componentId, specData);
                case TITLE -> buildTitleSpec(componentId, specData);
                case IMAGE -> buildImageSpec(componentId, specData);
                case BLANK -> buildBlankSpec(componentId, specData);
                case PRODUCT ->
                        new ComponentSpec.ProductSpec(
                                componentId, entity.getExposedProducts(), List.of(), List.of());
                case CATEGORY ->
                        buildCategorySpec(componentId, entity.getExposedProducts(), specData);
                case BRAND -> buildBrandSpec(componentId, entity.getExposedProducts(), specData);
                case TAB -> buildTabSpec(componentId, entity.getExposedProducts(), specData, tabs);
            };
        } catch (Exception e) {
            return null;
        }
    }

    private ComponentSpec.TextSpec buildTextSpec(long componentId, String specData)
            throws Exception {
        if (specData == null || specData.isBlank()) {
            return new ComponentSpec.TextSpec(componentId, "");
        }
        JsonNode node = objectMapper.readTree(specData);
        String content = node.path("content").asText("");
        return new ComponentSpec.TextSpec(componentId, content);
    }

    private ComponentSpec.TitleSpec buildTitleSpec(long componentId, String specData)
            throws Exception {
        if (specData == null || specData.isBlank()) {
            return new ComponentSpec.TitleSpec(componentId, "", null, null, null);
        }
        JsonNode node = objectMapper.readTree(specData);
        String title1 = node.path("title1").asText(null);
        String title2 = node.path("title2").asText(null);
        String subTitle1 = node.path("subTitle1").asText(null);
        String subTitle2 = node.path("subTitle2").asText(null);
        return new ComponentSpec.TitleSpec(componentId, title1, title2, subTitle1, subTitle2);
    }

    private ComponentSpec.ImageSpec buildImageSpec(long componentId, String specData)
            throws Exception {
        if (specData == null || specData.isBlank()) {
            return new ComponentSpec.ImageSpec(componentId, ImageType.SINGLE, List.of());
        }
        JsonNode node = objectMapper.readTree(specData);
        ImageType imageType =
                parseEnum(ImageType.class, node.path("imageType").asText(null), ImageType.SINGLE);

        List<ImageSlide> slides = new ArrayList<>();
        JsonNode slidesNode = node.path("slides");
        if (slidesNode.isArray()) {
            for (JsonNode slideNode : slidesNode) {
                slides.add(
                        new ImageSlide(
                                slideNode.path("imageComponentItemId").asLong(0),
                                slideNode.path("displayOrder").asInt(0),
                                slideNode.path("imageUrl").asText(null),
                                slideNode.path("linkUrl").asText(null)));
            }
        }
        return new ComponentSpec.ImageSpec(componentId, imageType, slides);
    }

    private ComponentSpec.BlankSpec buildBlankSpec(long componentId, String specData)
            throws Exception {
        if (specData == null || specData.isBlank()) {
            return new ComponentSpec.BlankSpec(componentId, 0.0, false);
        }
        JsonNode node = objectMapper.readTree(specData);
        double height = node.path("height").asDouble(0.0);
        boolean showLine = node.path("showLine").asBoolean(false);
        return new ComponentSpec.BlankSpec(componentId, height, showLine);
    }

    private ComponentSpec.CategorySpec buildCategorySpec(
            long componentId, int exposedProducts, String specData) throws Exception {
        if (specData == null || specData.isBlank()) {
            return new ComponentSpec.CategorySpec(
                    componentId, 0L, exposedProducts, List.of(), List.of());
        }
        JsonNode node = objectMapper.readTree(specData);
        long categoryId = node.path("categoryId").asLong(0L);
        return new ComponentSpec.CategorySpec(
                componentId, categoryId, exposedProducts, List.of(), List.of());
    }

    private ComponentSpec.BrandSpec buildBrandSpec(
            long componentId, int exposedProducts, String specData) throws Exception {
        if (specData == null || specData.isBlank()) {
            return new ComponentSpec.BrandSpec(
                    componentId, 0L, exposedProducts, List.of(), List.of(), List.of());
        }
        JsonNode node = objectMapper.readTree(specData);
        long categoryId = node.path("categoryId").asLong(0L);

        List<BrandFilter> brandFilters = new ArrayList<>();
        JsonNode filtersNode = node.path("brandFilters");
        if (filtersNode.isArray()) {
            for (JsonNode filterNode : filtersNode) {
                brandFilters.add(
                        new BrandFilter(
                                filterNode.path("brandId").asLong(0),
                                filterNode.path("brandName").asText(null)));
            }
        }
        return new ComponentSpec.BrandSpec(
                componentId, categoryId, exposedProducts, brandFilters, List.of(), List.of());
    }

    private ComponentSpec.TabSpec buildTabSpec(
            long componentId,
            int exposedProducts,
            String specData,
            List<DisplayTabJpaEntity> tabEntities)
            throws Exception {
        boolean sticky = false;
        TabMovingType movingType = TabMovingType.SCROLL;

        if (specData != null && !specData.isBlank()) {
            JsonNode node = objectMapper.readTree(specData);
            sticky = node.path("sticky").asBoolean(false);
            movingType =
                    parseEnum(
                            TabMovingType.class,
                            node.path("movingType").asText(null),
                            TabMovingType.SCROLL);
        }

        List<DisplayTab> tabs = new ArrayList<>();
        if (tabEntities != null) {
            for (DisplayTabJpaEntity tabEntity : tabEntities) {
                tabs.add(
                        new DisplayTab(
                                tabEntity.getId(),
                                tabEntity.getTabName(),
                                tabEntity.getDisplayOrder(),
                                List.of(),
                                List.of()));
            }
        }

        return new ComponentSpec.TabSpec(componentId, exposedProducts, sticky, movingType, tabs);
    }

    private String serializeComponentSpec(ComponentSpec spec) {
        if (spec == null) {
            return null;
        }
        try {
            return switch (spec) {
                case ComponentSpec.TextSpec ts ->
                        objectMapper.writeValueAsString(Map.of("content", ts.content()));
                case ComponentSpec.TitleSpec ts ->
                        objectMapper.writeValueAsString(
                                filterNulls(
                                        Map.of(
                                                "title1", orEmpty(ts.title1()),
                                                "title2", orEmpty(ts.title2()),
                                                "subTitle1", orEmpty(ts.subTitle1()),
                                                "subTitle2", orEmpty(ts.subTitle2()))));
                case ComponentSpec.ImageSpec is ->
                        objectMapper.writeValueAsString(
                                Map.of("imageType", is.imageType().name(), "slides", is.slides()));
                case ComponentSpec.BlankSpec bs ->
                        objectMapper.writeValueAsString(
                                Map.of("height", bs.height(), "showLine", bs.showLine()));
                case ComponentSpec.CategorySpec cs ->
                        objectMapper.writeValueAsString(Map.of("categoryId", cs.categoryId()));
                case ComponentSpec.BrandSpec bs ->
                        objectMapper.writeValueAsString(
                                Map.of(
                                        "categoryId", bs.categoryId(),
                                        "brandFilters", bs.brandFilters()));
                case ComponentSpec.TabSpec ts ->
                        objectMapper.writeValueAsString(
                                Map.of(
                                        "sticky", ts.sticky(),
                                        "movingType", ts.movingType().name()));
                case ComponentSpec.ProductSpec ps -> null;
            };
        } catch (Exception e) {
            return null;
        }
    }

    private int resolveExposedProducts(ComponentSpec spec) {
        if (spec == null) {
            return 0;
        }
        return switch (spec) {
            case ComponentSpec.ProductSpec ps -> ps.exposedProducts();
            case ComponentSpec.CategorySpec cs -> cs.exposedProducts();
            case ComponentSpec.BrandSpec bs -> bs.exposedProducts();
            case ComponentSpec.TabSpec ts -> ts.exposedProducts();
            default -> 0;
        };
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

    private String orEmpty(String value) {
        return value != null ? value : "";
    }

    private Map<String, Object> filterNulls(Map<String, Object> map) {
        return map.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
