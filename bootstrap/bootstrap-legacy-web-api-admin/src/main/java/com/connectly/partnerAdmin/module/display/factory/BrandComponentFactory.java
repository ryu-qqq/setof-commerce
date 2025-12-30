package com.connectly.partnerAdmin.module.display.factory;


import com.connectly.partnerAdmin.module.display.dto.component.brand.BrandComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.factory.BrandComponentFactoryDto;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import org.springframework.stereotype.Component;

@Component
public class BrandComponentFactory implements ComponentFactory<BrandComponentDetail, BrandComponentFactoryDto>{
    @Override
    public ComponentType getComponentType() {
        return ComponentType.BRAND;
    }

    @Override
    public BrandComponentDetail makeSubComponent(BrandComponentFactoryDto dto) {
        return dto.toComponentDetail();
    }

}
