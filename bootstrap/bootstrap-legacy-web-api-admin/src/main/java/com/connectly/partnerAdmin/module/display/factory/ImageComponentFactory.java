package com.connectly.partnerAdmin.module.display.factory;


import com.connectly.partnerAdmin.module.display.dto.component.image.ImageComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.factory.ImageComponentFactoryDto;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import org.springframework.stereotype.Component;

@Component
public class ImageComponentFactory implements ComponentFactory<ImageComponentDetail, ImageComponentFactoryDto>{
    @Override
    public ComponentType getComponentType() {
        return ComponentType.IMAGE;
    }

    @Override
    public ImageComponentDetail makeSubComponent(ImageComponentFactoryDto dto) {
        return dto.toComponentDetail();
    }
}
