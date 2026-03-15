package com.ryuqq.setof.storage.legacy.composite.content.adapter;

import com.ryuqq.setof.application.contentpage.port.out.DisplayComponentQueryPort;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import com.ryuqq.setof.domain.contentpage.vo.ComponentSpec;
import com.ryuqq.setof.domain.contentpage.vo.ProductSlot;
import com.ryuqq.setof.domain.legacy.content.dto.query.LegacyContentSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebBlankComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebBrandComponentItemQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebBrandComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebCategoryComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebComponentProductQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebImageComponentItemQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebImageComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebProductComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebTabComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebTabQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebTextComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebTitleComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebViewExtensionQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.mapper.LegacyWebContentMapper;
import com.ryuqq.setof.storage.legacy.composite.content.repository.LegacyWebContentCompositeQueryDslRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * LegacyWebDisplayComponentQueryAdapter - 디스플레이 컴포넌트 조회 Adapter.
 *
 * <p>DisplayComponentQueryPort를 구현하여 컴포넌트 메타 + ViewExtension + ComponentSpec을 조회한다.
 *
 * <p>활성화 조건: persistence.legacy.content.enabled=true
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.content.enabled", havingValue = "true")
public class LegacyWebDisplayComponentQueryAdapter implements DisplayComponentQueryPort {

    private final LegacyWebContentCompositeQueryDslRepository repository;
    private final LegacyWebContentMapper mapper;

    public LegacyWebDisplayComponentQueryAdapter(
            LegacyWebContentCompositeQueryDslRepository repository, LegacyWebContentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<DisplayComponent> fetchDisplayComponents(ContentPageSearchCriteria criteria) {
        LegacyContentSearchCondition condition =
                LegacyContentSearchCondition.of(criteria.contentPageId(), criteria.bypass());
        List<LegacyWebComponentQueryDto> components =
                deduplicateByDisplayOrder(repository.fetchComponentsByContentId(condition));

        List<Long> viewExtensionIds =
                components.stream()
                        .map(LegacyWebComponentQueryDto::viewExtensionId)
                        .filter(Objects::nonNull)
                        .toList();

        List<LegacyWebViewExtensionQueryDto> viewExtensions =
                repository.fetchViewExtensionsByIds(viewExtensionIds);
        Map<Long, LegacyWebViewExtensionQueryDto> viewExtensionMap =
                mapper.toViewExtensionMap(viewExtensions);

        Map<Long, ComponentSpec> componentSpecMap = buildComponentSpecMap(components);

        return mapper.toDisplayComponents(components, viewExtensionMap, componentSpecMap);
    }

    private Map<Long, ComponentSpec> buildComponentSpecMap(
            List<LegacyWebComponentQueryDto> components) {
        Map<String, List<LegacyWebComponentQueryDto>> byType =
                components.stream()
                        .collect(
                                Collectors.groupingBy(
                                        dto ->
                                                dto.componentType() != null
                                                        ? dto.componentType()
                                                        : ""));

        Map<Long, ComponentSpec> specMap = new HashMap<>();

        List<LegacyWebComponentQueryDto> titleComponents = byType.getOrDefault("TITLE", List.of());
        if (!titleComponents.isEmpty()) {
            List<Long> ids =
                    titleComponents.stream().map(LegacyWebComponentQueryDto::componentId).toList();
            List<LegacyWebTitleComponentQueryDto> titles = repository.fetchTitleComponents(ids);
            for (LegacyWebTitleComponentQueryDto dto : titles) {
                specMap.put(dto.componentId(), mapper.toTitleSpec(dto));
            }
        }

        List<LegacyWebComponentQueryDto> textComponents = byType.getOrDefault("TEXT", List.of());
        if (!textComponents.isEmpty()) {
            List<Long> ids =
                    textComponents.stream().map(LegacyWebComponentQueryDto::componentId).toList();
            List<LegacyWebTextComponentQueryDto> texts = repository.fetchTextComponents(ids);
            for (LegacyWebTextComponentQueryDto dto : texts) {
                specMap.put(dto.componentId(), mapper.toTextSpec(dto));
            }
        }

        List<LegacyWebComponentQueryDto> blankComponents = byType.getOrDefault("BLANK", List.of());
        if (!blankComponents.isEmpty()) {
            List<Long> ids =
                    blankComponents.stream().map(LegacyWebComponentQueryDto::componentId).toList();
            List<LegacyWebBlankComponentQueryDto> blanks = repository.fetchBlankComponents(ids);
            for (LegacyWebBlankComponentQueryDto dto : blanks) {
                specMap.put(dto.componentId(), mapper.toBlankSpec(dto));
            }
        }

        List<LegacyWebComponentQueryDto> imageComponents = byType.getOrDefault("IMAGE", List.of());
        if (!imageComponents.isEmpty()) {
            List<Long> ids =
                    imageComponents.stream().map(LegacyWebComponentQueryDto::componentId).toList();
            List<LegacyWebImageComponentQueryDto> images = repository.fetchImageComponents(ids);

            List<Long> imageComponentIds =
                    images.stream().map(LegacyWebImageComponentQueryDto::imageComponentId).toList();
            List<LegacyWebImageComponentItemQueryDto> items =
                    repository.fetchImageComponentItems(imageComponentIds);
            Map<Long, List<LegacyWebImageComponentItemQueryDto>> itemsByImageComponentId =
                    items.stream()
                            .collect(
                                    Collectors.groupingBy(
                                            LegacyWebImageComponentItemQueryDto::imageComponentId));

            for (LegacyWebImageComponentQueryDto img : images) {
                List<LegacyWebImageComponentItemQueryDto> imgItems =
                        itemsByImageComponentId.getOrDefault(img.imageComponentId(), List.of());
                specMap.put(img.componentId(), mapper.toImageSpec(img, imgItems));
            }
        }

        buildProductComponentSpecs(byType, specMap);
        buildBrandComponentSpecs(byType, specMap);
        buildCategoryComponentSpecs(byType, specMap);
        buildTabComponentSpecs(byType, specMap);

        return specMap;
    }

    private void buildProductComponentSpecs(
            Map<String, List<LegacyWebComponentQueryDto>> byType,
            Map<Long, ComponentSpec> specMap) {
        List<LegacyWebComponentQueryDto> productComponents =
                byType.getOrDefault("PRODUCT", List.of());
        if (productComponents.isEmpty()) {
            return;
        }
        List<Long> ids =
                productComponents.stream().map(LegacyWebComponentQueryDto::componentId).toList();
        List<LegacyWebProductComponentQueryDto> products = repository.fetchProductComponents(ids);
        Map<Long, Integer> exposedProductsMap =
                productComponents.stream()
                        .collect(
                                Collectors.toMap(
                                        LegacyWebComponentQueryDto::componentId,
                                        LegacyWebComponentQueryDto::exposedProducts));
        List<LegacyWebComponentProductQueryDto> componentProducts =
                repository.fetchComponentProducts(ids);
        Map<Long, List<ProductSlot>> slotsByComponent =
                toProductSlotsByComponent(componentProducts);

        for (LegacyWebProductComponentQueryDto dto : products) {
            int exposed = exposedProductsMap.getOrDefault(dto.componentId(), 0);
            List<ProductSlot> slots = slotsByComponent.getOrDefault(dto.componentId(), List.of());
            specMap.put(dto.componentId(), mapper.toProductSpec(dto, exposed, slots, List.of()));
        }
    }

    private void buildBrandComponentSpecs(
            Map<String, List<LegacyWebComponentQueryDto>> byType,
            Map<Long, ComponentSpec> specMap) {
        List<LegacyWebComponentQueryDto> brandComponents = byType.getOrDefault("BRAND", List.of());
        if (brandComponents.isEmpty()) {
            return;
        }
        List<Long> ids =
                brandComponents.stream().map(LegacyWebComponentQueryDto::componentId).toList();
        List<LegacyWebBrandComponentQueryDto> brands = repository.fetchBrandComponents(ids);
        Map<Long, Integer> exposedProductsMap =
                brandComponents.stream()
                        .collect(
                                Collectors.toMap(
                                        LegacyWebComponentQueryDto::componentId,
                                        LegacyWebComponentQueryDto::exposedProducts));

        List<Long> brandComponentIds =
                brands.stream().map(LegacyWebBrandComponentQueryDto::brandComponentId).toList();
        List<LegacyWebBrandComponentItemQueryDto> brandItems =
                repository.fetchBrandComponentItems(brandComponentIds);
        Map<Long, List<LegacyWebBrandComponentItemQueryDto>> itemsByBrandComponent =
                brandItems.stream()
                        .collect(
                                Collectors.groupingBy(
                                        LegacyWebBrandComponentItemQueryDto::brandComponentId));

        List<LegacyWebComponentProductQueryDto> componentProducts =
                repository.fetchComponentProducts(ids);
        Map<Long, List<ProductSlot>> slotsByComponent =
                toProductSlotsByComponent(componentProducts);

        for (LegacyWebBrandComponentQueryDto dto : brands) {
            int exposed = exposedProductsMap.getOrDefault(dto.componentId(), 0);
            List<LegacyWebBrandComponentItemQueryDto> items =
                    itemsByBrandComponent.getOrDefault(dto.brandComponentId(), List.of());
            List<ProductSlot> slots = slotsByComponent.getOrDefault(dto.componentId(), List.of());
            specMap.put(
                    dto.componentId(), mapper.toBrandSpec(dto, exposed, items, slots, List.of()));
        }
    }

    private void buildCategoryComponentSpecs(
            Map<String, List<LegacyWebComponentQueryDto>> byType,
            Map<Long, ComponentSpec> specMap) {
        List<LegacyWebComponentQueryDto> categoryComponents =
                byType.getOrDefault("CATEGORY", List.of());
        if (categoryComponents.isEmpty()) {
            return;
        }
        List<Long> ids =
                categoryComponents.stream().map(LegacyWebComponentQueryDto::componentId).toList();
        List<LegacyWebCategoryComponentQueryDto> categories =
                repository.fetchCategoryComponents(ids);
        Map<Long, Integer> exposedProductsMap =
                categoryComponents.stream()
                        .collect(
                                Collectors.toMap(
                                        LegacyWebComponentQueryDto::componentId,
                                        LegacyWebComponentQueryDto::exposedProducts));
        List<LegacyWebComponentProductQueryDto> componentProducts =
                repository.fetchComponentProducts(ids);
        Map<Long, List<ProductSlot>> slotsByComponent =
                toProductSlotsByComponent(componentProducts);

        for (LegacyWebCategoryComponentQueryDto dto : categories) {
            int exposed = exposedProductsMap.getOrDefault(dto.componentId(), 0);
            List<ProductSlot> slots = slotsByComponent.getOrDefault(dto.componentId(), List.of());
            specMap.put(dto.componentId(), mapper.toCategorySpec(dto, exposed, slots, List.of()));
        }
    }

    private void buildTabComponentSpecs(
            Map<String, List<LegacyWebComponentQueryDto>> byType,
            Map<Long, ComponentSpec> specMap) {
        List<LegacyWebComponentQueryDto> tabComponents = byType.getOrDefault("TAB", List.of());
        if (tabComponents.isEmpty()) {
            return;
        }
        List<Long> ids =
                tabComponents.stream().map(LegacyWebComponentQueryDto::componentId).toList();
        List<LegacyWebTabComponentQueryDto> tabComps = repository.fetchTabComponents(ids);

        List<Long> tabComponentIds =
                tabComps.stream().map(LegacyWebTabComponentQueryDto::tabComponentId).toList();
        List<LegacyWebTabQueryDto> tabs = repository.fetchTabs(tabComponentIds);
        Map<Long, List<LegacyWebTabQueryDto>> tabsByComponent =
                tabs.stream().collect(Collectors.groupingBy(LegacyWebTabQueryDto::tabComponentId));

        List<LegacyWebComponentProductQueryDto> componentProducts =
                repository.fetchComponentProducts(ids);
        Map<Long, List<ProductSlot>> slotsByTab =
                componentProducts.stream()
                        .filter(dto -> dto.tabId() != 0L)
                        .collect(
                                Collectors.groupingBy(
                                        LegacyWebComponentProductQueryDto::tabId,
                                        Collectors.mapping(
                                                dto ->
                                                        new ProductSlot(
                                                                dto.productGroupId(),
                                                                dto.displayOrder() != null
                                                                        ? dto.displayOrder()
                                                                                .intValue()
                                                                        : 0),
                                                Collectors.toList())));

        Map<Long, Integer> tabExposedMap =
                tabComponents.stream()
                        .collect(
                                Collectors.toMap(
                                        LegacyWebComponentQueryDto::componentId,
                                        LegacyWebComponentQueryDto::exposedProducts));

        for (LegacyWebTabComponentQueryDto dto : tabComps) {
            List<LegacyWebTabQueryDto> tabList =
                    tabsByComponent.getOrDefault(dto.tabComponentId(), List.of());
            int exposed = tabExposedMap.getOrDefault(dto.componentId(), 0);
            specMap.put(
                    dto.componentId(),
                    mapper.toTabSpec(dto, tabList, slotsByTab, Map.of(), exposed));
        }
    }

    /**
     * 동일 (displayOrder, componentType) 중복 시 첫 번째 컴포넌트만 선택.
     *
     * <p>레거시 호환: 같은 위치에 동일 타입 컴포넌트가 중복 등록된 경우(삭제 누락) 먼저 등록된(componentId가 작은) 것만 유지합니다.
     */
    private List<LegacyWebComponentQueryDto> deduplicateByDisplayOrder(
            List<LegacyWebComponentQueryDto> components) {
        Map<String, LegacyWebComponentQueryDto> deduped = new LinkedHashMap<>();
        for (LegacyWebComponentQueryDto dto : components) {
            String key = dto.displayOrder() + ":" + dto.componentType();
            deduped.putIfAbsent(key, dto);
        }
        return new ArrayList<>(deduped.values());
    }

    private Map<Long, List<ProductSlot>> toProductSlotsByComponent(
            List<LegacyWebComponentProductQueryDto> products) {
        return products.stream()
                .filter(dto -> dto.tabId() == 0L)
                .collect(
                        Collectors.groupingBy(
                                LegacyWebComponentProductQueryDto::componentId,
                                Collectors.mapping(
                                        dto ->
                                                new ProductSlot(
                                                        dto.productGroupId(),
                                                        dto.displayOrder() != null
                                                                ? dto.displayOrder().intValue()
                                                                : 0),
                                        Collectors.toList())));
    }
}
