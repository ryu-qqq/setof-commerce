package com.setof.connectly.module.display.service.component.fetch.text;

import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.factory.TextComponentFactoryDto;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.display.dto.query.text.TextComponentQueryDto;
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
public class TextComponentFindService
        extends BaseNonProductComponentFindService<TextComponentQueryDto> {

    public TextComponentFindService(ComponentFactoryStrategy componentFactoryStrategy) {
        super(componentFactoryStrategy);
    }

    @Override
    public Map<Integer, SubComponent> fetchComponents(
            List<TextComponentQueryDto> textComponentQueries,
            List<ComponentQueryDto> componentQueries) {
        Map<Integer, SubComponent> results = new HashMap<>();

        Map<Long, TextComponentQueryDto> componentIdMap = toMap(textComponentQueries);
        Map<Long, Integer> orderMap = displayOrderMap(componentQueries);
        Map<Long, ComponentQueryDto> contentQueryDtoMap = componentQueryMap(componentQueries);

        for (Long componentId : componentIdMap.keySet()) {
            TextComponentQueryDto textComponentQueryDto = componentIdMap.get(componentId);
            ComponentQueryDto componentQueryDto = contentQueryDtoMap.get(componentId);

            TextComponentFactoryDto textComponentFactoryDto =
                    TextComponentFactoryDto.builder()
                            .componentQueryDto(componentQueryDto)
                            .query(textComponentQueryDto)
                            .build();

            results.put(orderMap.get(componentId), toComponentDetail(textComponentFactoryDto));
        }

        return results;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TEXT;
    }
}
