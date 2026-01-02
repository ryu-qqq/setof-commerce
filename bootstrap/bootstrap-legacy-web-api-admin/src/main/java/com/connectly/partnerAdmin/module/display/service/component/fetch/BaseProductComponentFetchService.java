package com.connectly.partnerAdmin.module.display.service.component.fetch;

import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentItemQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQuery;
import com.connectly.partnerAdmin.module.display.factory.ComponentFactoryStrategy;
import com.connectly.partnerAdmin.module.display.mapper.item.ComponentItemMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BaseProductComponentFetchService<T extends ComponentQuery> extends BaseComponentFetchService implements SubProductComponentFetchService<T> {
    private final ComponentItemMapper componentItemMapper;

    public BaseProductComponentFetchService(ComponentFactoryStrategy componentFactoryStrategy, ComponentItemMapper componentItemMapper) {
        super(componentFactoryStrategy);
        this.componentItemMapper = componentItemMapper;
    }

    protected List<SortItem> transProductGroupThumbnail(List<ComponentItemQueryDto> componentItemQueries){
        return componentItemMapper.transProductGroupThumbnail(componentItemQueries);
    }


    protected Map<Long, List<ComponentItemQueryDto>> componentIdMap(List<ComponentItemQueryDto> componentItemQueries){
        return componentItemQueries.stream().collect(Collectors.groupingBy(ComponentItemQueryDto::getComponentId));
    }





}
