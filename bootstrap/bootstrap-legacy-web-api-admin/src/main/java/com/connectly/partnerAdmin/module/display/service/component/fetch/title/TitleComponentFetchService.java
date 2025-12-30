package com.connectly.partnerAdmin.module.display.service.component.fetch.title;


import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.factory.TitleComponentFactoryDto;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.title.TitleComponentQueryDto;
import com.connectly.partnerAdmin.module.display.entity.component.sub.TitleComponent;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.factory.ComponentFactoryStrategy;
import com.connectly.partnerAdmin.module.display.repository.component.title.TitleRepository;
import com.connectly.partnerAdmin.module.display.service.component.fetch.BaseNonProductComponentFetchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
@Service
public class TitleComponentFetchService extends BaseNonProductComponentFetchService<TitleComponentQueryDto> {

    private final TitleRepository titleRepository;

    public TitleComponentFetchService(ComponentFactoryStrategy componentFactoryStrategy, TitleRepository titleRepository) {
        super(componentFactoryStrategy);
        this.titleRepository = titleRepository;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TITLE;
    }

    @Override
    public Map<Integer, SubComponent> fetchComponents(List<TitleComponentQueryDto> titleComponentQueries, List<ComponentQueryDto> componentQueries) {
        Map<Integer, SubComponent> results = new HashMap<>();

        Map<Long, TitleComponentQueryDto> componentIdMap = toMap(titleComponentQueries);
        Map<Long, Integer> orderMap = displayOrderMap(componentQueries);
        Map<Long, ComponentQueryDto> componentQueryDtoMap = componentQueryMap(componentQueries);

        for(Long componentId : componentIdMap.keySet()){
            TitleComponentQueryDto titleComponentQueryDto = componentIdMap.get(componentId);
            ComponentQueryDto componentQueryDto = componentQueryDtoMap.get(componentId);

            TitleComponentFactoryDto titleComponentFactoryDto = TitleComponentFactoryDto.builder()
                    .componentQueryDto(componentQueryDto)
                    .query(titleComponentQueryDto)
                    .build();

            results.put(orderMap.get(componentId), toComponentDetail(titleComponentFactoryDto));
        }

        return results;
    }

    public List<TitleComponent> fetchTitleComponentEntities(List<Long> titleComponentIds){
        return titleRepository.findAllById(titleComponentIds);
    }
}
