package com.setof.connectly.module.display.service.component.fetch.image;

import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.factory.ImageComponentFactoryDto;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.display.dto.query.image.ImageComponentQueryDto;
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
public class ImageComponentFindService
        extends BaseNonProductComponentFindService<ImageComponentQueryDto> {

    public ImageComponentFindService(ComponentFactoryStrategy componentFactoryStrategy) {
        super(componentFactoryStrategy);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.IMAGE;
    }

    @Override
    public Map<Integer, SubComponent> fetchComponents(
            List<ImageComponentQueryDto> imageComponentQueries,
            List<ComponentQueryDto> componentQueries) {

        Map<Long, List<ImageComponentQueryDto>> componentIdMap = toListMap(imageComponentQueries);

        Map<Integer, SubComponent> results = new HashMap<>();
        Map<Long, Integer> orderMap = displayOrderMap(componentQueries);
        Map<Long, ComponentQueryDto> contentQueryDtoMap = componentQueryMap(componentQueries);

        for (Long componentId : componentIdMap.keySet()) {
            List<ImageComponentQueryDto> imageComponentQueryDtos = componentIdMap.get(componentId);
            ComponentQueryDto componentQueryDto = contentQueryDtoMap.get(componentId);

            ImageComponentFactoryDto build =
                    ImageComponentFactoryDto.builder()
                            .componentQueryDto(componentQueryDto)
                            .queries(imageComponentQueryDtos)
                            .build();

            results.put(orderMap.get(componentId), toComponentDetail(build));
        }
        return results;
    }
}
