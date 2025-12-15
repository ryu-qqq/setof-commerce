package com.setof.connectly.module.display.service.component.fetch.title;

import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.factory.TitleComponentFactoryDto;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.display.dto.query.title.TitleComponentQueryDto;
import com.setof.connectly.module.display.enums.component.ComponentType;
import com.setof.connectly.module.display.factory.ComponentFactoryStrategy;
import com.setof.connectly.module.display.service.component.fetch.BaseNonProductComponentFindService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TitleComponentFindService
        extends BaseNonProductComponentFindService<TitleComponentQueryDto> {

    public TitleComponentFindService(ComponentFactoryStrategy componentFactoryStrategy) {
        super(componentFactoryStrategy);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TITLE;
    }

    @Override
    public Map<Integer, SubComponent> fetchComponents(
            List<TitleComponentQueryDto> titleComponentQueries,
            List<ComponentQueryDto> componentQueries) {
        Map<Integer, SubComponent> results = new HashMap<>();

        Map<Long, TitleComponentQueryDto> componentIdMap = toMap(titleComponentQueries);
        Map<Long, Integer> orderMap = displayOrderMap(componentQueries);
        Map<Long, ComponentQueryDto> componentQueryDtoMap = componentQueryMap(componentQueries);

        for (Long componentId : componentIdMap.keySet()) {
            TitleComponentQueryDto titleComponentQueryDto = componentIdMap.get(componentId);
            ComponentQueryDto componentQueryDto = componentQueryDtoMap.get(componentId);

            TitleComponentFactoryDto titleComponentFactoryDto =
                    TitleComponentFactoryDto.builder()
                            .componentQueryDto(componentQueryDto)
                            .query(titleComponentQueryDto)
                            .build();

            results.put(orderMap.get(componentId), toComponentDetail(titleComponentFactoryDto));
        }

        return results;
    }
}
