package com.connectly.partnerAdmin.module.display.service.component.fetch.blank;

import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.factory.BlankComponentFactoryDto;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.blank.BlankComponentQueryDto;
import com.connectly.partnerAdmin.module.display.entity.component.sub.BlankComponent;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.factory.ComponentFactoryStrategy;
import com.connectly.partnerAdmin.module.display.repository.component.blank.BlankComponentRepository;
import com.connectly.partnerAdmin.module.display.service.component.fetch.BaseNonProductComponentFetchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Transactional(readOnly = true)
@Service
public class BlankComponentFetchService extends BaseNonProductComponentFetchService<BlankComponentQueryDto> {

    private final BlankComponentRepository blankComponentRepository;

    public BlankComponentFetchService(ComponentFactoryStrategy componentFactoryStrategy, BlankComponentRepository blankComponentRepository) {
        super(componentFactoryStrategy);
        this.blankComponentRepository = blankComponentRepository;
    }

    @Override
    public Map<Integer, SubComponent> fetchComponents(List<BlankComponentQueryDto> blankComponentQueries, List<ComponentQueryDto> componentQueries) {

        Map<Long, BlankComponentQueryDto> componentIdMap = toMap(blankComponentQueries);

        Map<Integer, SubComponent> results = new HashMap<>();
        Map<Long, Integer> orderMap = displayOrderMap(componentQueries);
        Map<Long, ComponentQueryDto> ComponentQueryDtoMap = componentQueryMap(componentQueries);

        for(Long componentId : componentIdMap.keySet()){
            ComponentQueryDto componentQuery = ComponentQueryDtoMap.get(componentId);
            BlankComponentQueryDto blankComponentQuery = componentIdMap.get(componentId);

            BlankComponentFactoryDto blankComponentFactoryDto = BlankComponentFactoryDto.builder()
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


    public List<BlankComponent> fetchBlankComponentEntities(List<Long> blankComponentIds){
        return blankComponentRepository.findAllById(blankComponentIds);
    }
}
