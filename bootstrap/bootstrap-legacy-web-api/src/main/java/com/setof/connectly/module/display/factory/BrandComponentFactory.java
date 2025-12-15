package com.setof.connectly.module.display.factory;

import com.setof.connectly.module.display.dto.component.brand.BrandComponentDetail;
import com.setof.connectly.module.display.dto.factory.BrandComponentFactoryDto;
import com.setof.connectly.module.display.enums.component.ComponentType;
import org.springframework.stereotype.Component;

@Component
public class BrandComponentFactory
        implements ComponentFactory<BrandComponentDetail, BrandComponentFactoryDto> {
    @Override
    public ComponentType getComponentType() {
        return ComponentType.BRAND;
    }

    @Override
    public BrandComponentDetail makeSubComponent(BrandComponentFactoryDto dto) {
        return dto.toComponentDetail();
    }
}
