package com.setof.connectly.module.display.service.component.fetch.tab;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.display.dto.component.SortItem;
import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.component.tab.TabDetail;
import com.setof.connectly.module.display.dto.factory.TabComponentFactoryDto;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.display.dto.filter.tab.TabProductFilter;
import com.setof.connectly.module.display.dto.query.ComponentItemQueryDto;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.display.dto.query.tab.TabComponentQueryDto;
import com.setof.connectly.module.display.enums.component.ComponentType;
import com.setof.connectly.module.display.factory.ComponentFactoryStrategy;
import com.setof.connectly.module.display.mapper.ComponentItemMapper;
import com.setof.connectly.module.display.service.component.fetch.BaseProductComponentFindService;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.mapper.ProductSliceMapper;
import com.setof.connectly.module.product.service.group.fetch.ProductGroupFindService;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TabComponentFindServiceImpl
        extends BaseProductComponentFindService<TabComponentQueryDto> {

    public TabComponentFindServiceImpl(
            ComponentFactoryStrategy componentFactoryStrategy,
            ProductSliceMapper productSliceMapper,
            ProductGroupFindService productGroupFindService,
            ComponentItemMapper componentItemMapper) {
        super(
                componentFactoryStrategy,
                productSliceMapper,
                productGroupFindService,
                componentItemMapper);
    }

    @Override
    public Map<Integer, SubComponent> fetchComponents(
            List<TabComponentQueryDto> productRelatedQueries,
            List<ComponentItemQueryDto> componentItemQueries,
            List<ComponentQueryDto> componentQueries) {

        Map<Integer, SubComponent> results = new HashMap<>();

        Map<Long, List<TabComponentQueryDto>> tabComponentQueryMap =
                toListMap(productRelatedQueries);
        Map<Long, List<ComponentItemQueryDto>> componentItemMap =
                componentIdMap(componentItemQueries);
        Map<Long, ComponentQueryDto> componentQueryMap = componentQueryMap(componentQueries);
        Map<Long, Integer> orderMap = displayOrderMap(componentQueries);

        for (Long componentId : tabComponentQueryMap.keySet()) {

            ComponentQueryDto componentQueryDto = componentQueryMap.get(componentId);
            TabComponentFactoryDto tabComponent =
                    TabComponentFactoryDto.builder()
                            .queries(tabComponentQueryMap.get(componentId))
                            .componentQueryDto(componentQueryDto)
                            .tabComponentId(
                                    tabComponentQueryMap
                                            .get(componentId)
                                            .get(0)
                                            .getTabComponentId())
                            .productGroupThumbnails(new ArrayList<>())
                            .build();

            List<TabDetail> tabDetails =
                    generateTabDetailsForComponent(
                            componentId, tabComponentQueryMap, componentItemMap);
            tabDetails.sort(Comparator.comparingInt(TabDetail::getDisplayOrder));
            tabComponent.setTabDetails(tabDetails);

            results.put(orderMap.get(componentId), toComponentDetail(tabComponent));
        }

        return results;
    }

    private List<TabDetail> generateTabDetailsForComponent(
            Long componentId,
            Map<Long, List<TabComponentQueryDto>> tabComponentQueryMap,
            Map<Long, List<ComponentItemQueryDto>> componentItemMap) {
        List<TabComponentQueryDto> queries = tabComponentQueryMap.get(componentId);
        Map<Long, TabComponentQueryDto> groupByTabId =
                queries.stream()
                        .collect(
                                Collectors.toMap(
                                        TabComponentQueryDto::getTabId, Function.identity()));
        Map<Long, List<ComponentItemQueryDto>> groupedItems =
                componentItemMap.getOrDefault(componentId, new ArrayList<>()).stream()
                        .collect(Collectors.groupingBy(ComponentItemQueryDto::getTabId));

        List<TabDetail> tabDetails = new ArrayList<>();

        for (long tabId : groupByTabId.keySet()) {
            TabComponentQueryDto tabComponentQuery = groupByTabId.get(tabId);
            List<ComponentItemQueryDto> items = groupedItems.getOrDefault(tabId, new ArrayList<>());

            List<SortItem> sortItems = new ArrayList<>();

            if (!items.isEmpty()) sortItems = distributeSortTypeProductGroupThumbnailsForTab(items);

            int size = items.size() == 0 ? 20 : items.size();

            List<ProductGroupThumbnail> productGroupThumbnails =
                    processSortItemsWithLastDomainId(
                            componentId,
                            sortItems,
                            toTabProductFilter(tabId),
                            PageRequest.of(0, size));

            tabDetails.add(
                    TabDetail.builder()
                            .tabId(tabComponentQuery.getTabId())
                            .tabName(tabComponentQuery.getTabName())
                            .displayOrder(tabComponentQuery.getDisplayOrder())
                            .stickyYn(tabComponentQuery.getStickyYn())
                            .tabMovingType(tabComponentQuery.getTabMovingType())
                            .productGroupThumbnails(productGroupThumbnails)
                            .build());
        }

        return tabDetails;
    }

    private List<SortItem> distributeSortTypeProductGroupThumbnailsForTab(
            List<ComponentItemQueryDto> items) {
        return transProductGroupThumbnail(items);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TAB;
    }

    @Override
    public CustomSlice<ProductGroupThumbnail> fetchComponentProductGroups(
            long componentId,
            Set<ComponentItemQueryDto> componentItemQueries,
            ComponentFilter filter,
            Pageable pageable) {
        Map<Long, List<ComponentItemQueryDto>> componentItemMap =
                componentIdMap(new ArrayList<>(componentItemQueries));
        Map<Long, List<ComponentItemQueryDto>> groupedItems =
                componentItemMap.getOrDefault(componentId, new ArrayList<>()).stream()
                        .collect(Collectors.groupingBy(ComponentItemQueryDto::getTabId));
        List<ComponentItemQueryDto> tabComponentItemQueries =
                groupedItems.getOrDefault(filter.getTabId(), new ArrayList<>());

        return super.fetchComponentProductGroups(
                componentId, new LinkedHashSet<>(tabComponentItemQueries), filter, pageable);
    }

    @Override
    protected long fetchProductCount(ComponentFilter filter, Set<Long> productIds) {
        return productIds.size();
    }

    protected TabProductFilter toTabProductFilter(long tabId) {
        return TabProductFilter.builder().tabId(tabId).build();
    }
}
