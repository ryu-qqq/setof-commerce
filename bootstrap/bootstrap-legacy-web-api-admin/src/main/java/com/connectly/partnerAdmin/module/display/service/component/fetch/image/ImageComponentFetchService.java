package com.connectly.partnerAdmin.module.display.service.component.fetch.image;

import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.factory.ImageComponentFactoryDto;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.image.ImageComponentQueryDto;
import com.connectly.partnerAdmin.module.display.entity.component.sub.ImageComponent;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.factory.ComponentFactoryStrategy;
import com.connectly.partnerAdmin.module.display.repository.component.image.ImageComponentRepository;
import com.connectly.partnerAdmin.module.display.service.component.fetch.BaseNonProductComponentFetchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
@Service
public class ImageComponentFetchService extends BaseNonProductComponentFetchService<ImageComponentQueryDto> {

    private final ImageComponentRepository imageComponentRepository;

    public ImageComponentFetchService(ComponentFactoryStrategy componentFactoryStrategy, ImageComponentRepository imageComponentRepository) {
        super(componentFactoryStrategy);
        this.imageComponentRepository = imageComponentRepository;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.IMAGE;
    }

    @Override
    public Map<Integer, SubComponent> fetchComponents(List<ImageComponentQueryDto> imageComponentQueries, List<ComponentQueryDto> componentQueries) {

        Map<Long, List<ImageComponentQueryDto>> componentIdMap = toListMap(imageComponentQueries);

        Map<Integer, SubComponent> results = new HashMap<>();
        Map<Long, Integer> orderMap = displayOrderMap(componentQueries);
        Map<Long, ComponentQueryDto> contentQueryDtoMap = componentQueryMap(componentQueries);

        for(Long componentId : componentIdMap.keySet()){
            List<ImageComponentQueryDto> imageComponentQueryDtos = componentIdMap.get(componentId);
            ComponentQueryDto componentQueryDto = contentQueryDtoMap.get(componentId);

            ImageComponentFactoryDto build = ImageComponentFactoryDto.builder()
                    .componentQueryDto(componentQueryDto)
                    .queries(imageComponentQueryDtos)
                    .build();

            results.put(orderMap.get(componentId), toComponentDetail(build));
        }
        return results;
    }

    public List<ImageComponent> fetchImageComponentEntities(List<Long> imageComponentIds){
        return imageComponentRepository.findAllById(imageComponentIds);
    }



}
