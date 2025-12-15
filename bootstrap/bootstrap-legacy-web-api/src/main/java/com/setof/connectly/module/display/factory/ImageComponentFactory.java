package com.setof.connectly.module.display.factory;

import com.setof.connectly.module.display.dto.component.image.ImageComponentDetail;
import com.setof.connectly.module.display.dto.factory.ImageComponentFactoryDto;
import com.setof.connectly.module.display.enums.component.ComponentType;
import org.springframework.stereotype.Component;

@Component
public class ImageComponentFactory
        implements ComponentFactory<ImageComponentDetail, ImageComponentFactoryDto> {
    @Override
    public ComponentType getComponentType() {
        return ComponentType.IMAGE;
    }

    @Override
    public ImageComponentDetail makeSubComponent(ImageComponentFactoryDto dto) {
        return dto.toComponentDetail();
    }
}
