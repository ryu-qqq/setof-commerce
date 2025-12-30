package com.connectly.partnerAdmin.module.display.factory;

import com.connectly.partnerAdmin.module.display.dto.component.blank.BlankComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.factory.BlankComponentFactoryDto;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import org.springframework.stereotype.Component;

@Component
public class BlankComponentFactory implements ComponentFactory<BlankComponentDetail, BlankComponentFactoryDto>{
    @Override
    public ComponentType getComponentType() {
        return ComponentType.BLANK;
    }

    @Override
    public BlankComponentDetail makeSubComponent(BlankComponentFactoryDto dto) {
        return dto.toComponentDetail();
    }
}
