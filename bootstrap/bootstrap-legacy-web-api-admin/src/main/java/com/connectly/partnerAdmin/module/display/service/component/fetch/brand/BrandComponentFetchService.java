package com.connectly.partnerAdmin.module.display.service.component.fetch.brand;

import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.factory.BrandComponentFactoryDto;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentItemQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.brand.BrandComponentQueryDto;
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
public class BrandComponentFetchService extends BaseProductComponentFetchService<BrandComponentQueryDto> {


    public BrandComponentFetchService(ComponentFactoryStrategy componentFactoryStrategy, ComponentItemMapper componentItemMapper) {
        super(componentFactoryStrategy, componentItemMapper);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BRAND;
    }

    @Override
    public Map<Integer, SubComponent> fetchComponents(List<BrandComponentQueryDto> productRelatedQueries, List<ComponentItemQueryDto> componentItemQueries, List<ComponentQueryDto> componentQueries) {
        Map<Integer, SubComponent> results = new HashMap<>();

        Map<Long, List<BrandComponentQueryDto>> brandComponentQueryMap = toListMap(productRelatedQueries);
        Map<Long, List<ComponentItemQueryDto>> componentItemMap = componentIdMap(componentItemQueries);
        Map<Long, ComponentQueryDto> componentQueryMap = componentQueryMap(componentQueries);
         Map<Long, Integer> orderMap = displayOrderMap(componentQueries);

        for (Long componentId : brandComponentQueryMap.keySet()) {

            List<SortItem> sortItems = determineProductGroupThumbnails(componentId, componentItemMap);

            BrandComponentFactoryDto brandComponentFactoryDto = BrandComponentFactoryDto.builder()
                    .componentQueryDto(componentQueryMap.get(componentId))
                    .queries(brandComponentQueryMap.get(componentId))
                    .sortItems(sortItems)
                    .build();

            results.put(orderMap.get(componentId), toComponentDetail(brandComponentFactoryDto));
        }

        return results;
    }



    private List<SortItem> determineProductGroupThumbnails(Long componentId, Map<Long, List<ComponentItemQueryDto>> componentItemMap) {
        List<ComponentItemQueryDto> componentItems = componentItemMap.getOrDefault(componentId, new ArrayList<>());
        return transProductGroupThumbnail(componentItems);
    }



}
