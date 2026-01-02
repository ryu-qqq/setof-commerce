package com.connectly.partnerAdmin.module.display.service.component.fetch.text;

import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.factory.TextComponentFactoryDto;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.text.TextComponentQueryDto;
import com.connectly.partnerAdmin.module.display.entity.component.sub.TextComponent;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.factory.ComponentFactoryStrategy;
import com.connectly.partnerAdmin.module.display.repository.component.text.TextRepository;
import com.connectly.partnerAdmin.module.display.service.component.fetch.BaseNonProductComponentFetchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Transactional(readOnly = true)
@Service
public class TextComponentFetchService extends BaseNonProductComponentFetchService<TextComponentQueryDto> {

    private final TextRepository textRepository;
    public TextComponentFetchService(ComponentFactoryStrategy componentFactoryStrategy, TextRepository textRepository) {
        super(componentFactoryStrategy);
        this.textRepository = textRepository;
    }

    @Override
    public Map<Integer, SubComponent> fetchComponents(List<TextComponentQueryDto> textComponentQueries, List<ComponentQueryDto> componentQueries) {
        Map<Integer, SubComponent> results = new HashMap<>();

        Map<Long, TextComponentQueryDto> componentIdMap = toMap(textComponentQueries);
        Map<Long, Integer> orderMap = displayOrderMap(componentQueries);
        Map<Long, ComponentQueryDto> contentQueryDtoMap = componentQueryMap(componentQueries);

        for(Long componentId : componentIdMap.keySet()){
            TextComponentQueryDto textComponentQueryDto = componentIdMap.get(componentId);
            ComponentQueryDto componentQueryDto = contentQueryDtoMap.get(componentId);

            TextComponentFactoryDto textComponentFactoryDto = TextComponentFactoryDto.builder()
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


    public List<TextComponent> fetchTextComponentEntities(List<Long> textComponentIds){
        return textRepository.findAllById(textComponentIds);
    }

}
