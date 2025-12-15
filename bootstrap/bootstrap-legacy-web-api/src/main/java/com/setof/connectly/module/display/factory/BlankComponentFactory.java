package com.setof.connectly.module.display.factory;

import com.setof.connectly.module.display.dto.component.blank.BlankComponentDetail;
import com.setof.connectly.module.display.dto.factory.BlankComponentFactoryDto;
import com.setof.connectly.module.display.enums.component.ComponentType;
import org.springframework.stereotype.Component;

@Component
public class BlankComponentFactory
        implements ComponentFactory<BlankComponentDetail, BlankComponentFactoryDto> {
    @Override
    public ComponentType getComponentType() {
        return ComponentType.BLANK;
    }

    @Override
    public BlankComponentDetail makeSubComponent(BlankComponentFactoryDto dto) {
        return dto.toComponentDetail();
    }
}
