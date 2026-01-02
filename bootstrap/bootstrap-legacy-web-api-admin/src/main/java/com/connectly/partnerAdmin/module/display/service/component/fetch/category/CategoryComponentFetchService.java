package com.connectly.partnerAdmin.module.display.service.component.fetch.category;

import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.factory.CategoryComponentFactoryDto;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentItemQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.category.CategoryComponentQueryDto;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.factory.ComponentFactoryStrategy;
import com.connectly.partnerAdmin.module.display.mapper.item.ComponentItemMapper;
import com.connectly.partnerAdmin.module.display.service.component.fetch.BaseProductComponentFetchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Transactional(readOnly = true)
@Service
public class CategoryComponentFetchService extends BaseProductComponentFetchService<CategoryComponentQueryDto> {


    public CategoryComponentFetchService(ComponentFactoryStrategy componentFactoryStrategy, ComponentItemMapper componentItemMapper) {
        super(componentFactoryStrategy, componentItemMapper);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.CATEGORY;
    }

    @Override
    public Map<Integer, SubComponent> fetchComponents(List<CategoryComponentQueryDto> productRelatedQueries, List<ComponentItemQueryDto> componentItemQueries, List<ComponentQueryDto> componentQueries) {

        Map<Integer, SubComponent> results = new HashMap<>();

        Map<Long, List<CategoryComponentQueryDto>> categoryComponentQueryMap =toListMap(productRelatedQueries);
        Map<Long, List<ComponentItemQueryDto>> componentItemMap = componentIdMap(componentItemQueries);
        Map<Long, ComponentQueryDto> componentQueryMap =  componentQueryMap(componentQueries);
        Map<Long, Integer> orderMap = displayOrderMap(componentQueries);

        for (Long componentId : categoryComponentQueryMap.keySet()) {
            List<SortItem> sortItems = determineProductGroupThumbnailsForCategory(componentId, componentItemMap);

            CategoryComponentFactoryDto categoryComponentFactoryDto = CategoryComponentFactoryDto.builder()
                    .componentQueryDto(componentQueryMap.get(componentId))
                    .queries(categoryComponentQueryMap.get(componentId))
                    .sortItems(sortItems)
                    .build();

            results.put(orderMap.get(componentId), toComponentDetail(categoryComponentFactoryDto));
        }


        return results;
    }


    private List<SortItem> determineProductGroupThumbnailsForCategory(Long componentId, Map<Long, List<ComponentItemQueryDto>> componentItemMap) {
        List<ComponentItemQueryDto> componentItems = componentItemMap.getOrDefault(componentId, new ArrayList<>());
        return transProductGroupThumbnail(componentItems);
    }


}
