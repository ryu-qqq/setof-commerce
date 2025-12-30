package com.connectly.partnerAdmin.module.display.service.component.fetch.tab;

import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.component.tab.TabDetail;
import com.connectly.partnerAdmin.module.display.dto.factory.TabComponentFactoryDto;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentItemQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.tab.TabComponentQueryDto;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.factory.ComponentFactoryStrategy;
import com.connectly.partnerAdmin.module.display.mapper.item.ComponentItemMapper;
import com.connectly.partnerAdmin.module.display.service.component.fetch.BaseProductComponentFetchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class TabComponentFetchServiceImpl extends BaseProductComponentFetchService<TabComponentQueryDto> {


    public TabComponentFetchServiceImpl(ComponentFactoryStrategy componentFactoryStrategy, ComponentItemMapper componentItemMapper) {
        super(componentFactoryStrategy, componentItemMapper);
    }

    @Override
    public Map<Integer, SubComponent> fetchComponents(List<TabComponentQueryDto> productRelatedQueries, List<ComponentItemQueryDto> componentItemQueries, List<ComponentQueryDto> componentQueries) {

        LinkedHashMap<Integer, SubComponent> results = new LinkedHashMap<>();

        Map<Long, List<TabComponentQueryDto>> tabComponentQueryMap =toListMap(productRelatedQueries);
        Map<Long, List<ComponentItemQueryDto>> componentItemMap = componentIdMap(componentItemQueries);
        Map<Long, ComponentQueryDto> componentQueryMap =  componentQueryMap(componentQueries);
        Map<Long, Integer> orderMap = displayOrderMap(componentQueries);

        for (Long componentId : tabComponentQueryMap.keySet()) {
            List<TabDetail> tabDetails = generateTabDetailsForComponent(
                    componentId,
                    tabComponentQueryMap,
                    componentItemMap
            );

            TabComponentFactoryDto tabComponent = TabComponentFactoryDto.builder()
                    .queries(tabComponentQueryMap.get(componentId))
                    .componentQueryDto(componentQueryMap.get(componentId))
                    .tabComponentId(tabComponentQueryMap.get(componentId).get(0).getTabComponentId())
                    .productGroupThumbnails(new ArrayList<>())
                    .tabDetails(tabDetails)
                    .build();

            results.put(orderMap.get(componentId), toComponentDetail(tabComponent));
        }

        return results;
    }

    private List<TabDetail> generateTabDetailsForComponent(
            Long componentId,
            Map<Long, List<TabComponentQueryDto>> tabComponentQueryMap,
            Map<Long, List<ComponentItemQueryDto>> componentItemMap
    ) {
        List<TabComponentQueryDto> queries = tabComponentQueryMap.get(componentId);
        Map<Long, TabComponentQueryDto> groupByTabId = queries.stream().collect(Collectors.toMap(TabComponentQueryDto::getTabId, Function.identity()));
        Map<Long, List<ComponentItemQueryDto>> groupedItems = componentItemMap.getOrDefault(componentId, new ArrayList<>())
                .stream().collect(Collectors.groupingBy(ComponentItemQueryDto::getTabId));

        List<TabDetail> tabDetails = new ArrayList<>();

        for (long tabId : groupByTabId.keySet()) {
            TabComponentQueryDto tabComponentQuery = groupByTabId.get(tabId);
            List<ComponentItemQueryDto> items = groupedItems.getOrDefault(tabId, new ArrayList<>());
            List<SortItem> sortItems = distributeSortTypeProductGroupThumbnailsForTab(items);

            tabDetails.add(TabDetail.builder()
                    .tabId(tabComponentQuery.getTabId())
                    .tabName(tabComponentQuery.getTabName())
                    .displayOrder(tabComponentQuery.getDisplayOrder())
                    .stickyYn(tabComponentQuery.getStickyYn())
                    .tabMovingType(tabComponentQuery.getTabMovingType())
                    .sortItems(sortItems)
                    .build());
        }

        return tabDetails;
    }

    private List<SortItem> distributeSortTypeProductGroupThumbnailsForTab(List<ComponentItemQueryDto> items){
        return transProductGroupThumbnail(items);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TAB;
    }

}


