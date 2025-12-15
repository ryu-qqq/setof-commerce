package com.setof.connectly.module.display.factory;

import com.setof.connectly.module.display.dto.component.title.TitleComponentDetail;
import com.setof.connectly.module.display.dto.factory.TitleComponentFactoryDto;
import com.setof.connectly.module.display.enums.component.ComponentType;
import org.springframework.stereotype.Component;

@Component
public class TitleComponentFactory
        implements ComponentFactory<TitleComponentDetail, TitleComponentFactoryDto> {
    @Override
    public ComponentType getComponentType() {
        return ComponentType.TITLE;
    }

    @Override
    public TitleComponentDetail makeSubComponent(TitleComponentFactoryDto dto) {
        return dto.toComponentDetail();
    }
}
