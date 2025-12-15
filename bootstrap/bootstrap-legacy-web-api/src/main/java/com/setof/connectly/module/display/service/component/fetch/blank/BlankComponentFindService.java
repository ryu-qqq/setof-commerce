package com.setof.connectly.module.display.service.component.fetch.blank;

import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.factory.BlankComponentFactoryDto;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.display.dto.query.blank.BlankComponentQueryDto;
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
public class BlankComponentFindService
        extends BaseNonProductComponentFindService<BlankComponentQueryDto> {

    public BlankComponentFindService(ComponentFactoryStrategy componentFactoryStrategy) {
        super(componentFactoryStrategy);
    }

    @Override
    public Map<Integer, SubComponent> fetchComponents(
            List<BlankComponentQueryDto> blankComponentQueries,
            List<ComponentQueryDto> componentQueries) {

        Map<Long, BlankComponentQueryDto> componentIdMap = toMap(blankComponentQueries);

        Map<Integer, SubComponent> results = new HashMap<>();
        Map<Long, Integer> orderMap = displayOrderMap(componentQueries);
        Map<Long, ComponentQueryDto> ComponentQueryDtoMap = componentQueryMap(componentQueries);

        for (Long componentId : componentIdMap.keySet()) {
            ComponentQueryDto componentQuery = ComponentQueryDtoMap.get(componentId);
            BlankComponentQueryDto blankComponentQuery = componentIdMap.get(componentId);

            BlankComponentFactoryDto blankComponentFactoryDto =
                    BlankComponentFactoryDto.builder()
                            .componentQueryDto(componentQuery)
                            .query(blankComponentQuery)
                            .build();

            results.put(orderMap.get(componentId), toComponentDetail(blankComponentFactoryDto));
        }

        return results;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BLANK;
    }
}
