package com.setof.connectly.module.display.factory;

import com.setof.connectly.module.display.dto.component.text.TextComponentDetail;
import com.setof.connectly.module.display.dto.factory.TextComponentFactoryDto;
import com.setof.connectly.module.display.enums.component.ComponentType;
import org.springframework.stereotype.Component;

@Component
public class TextComponentFactory
        implements ComponentFactory<TextComponentDetail, TextComponentFactoryDto> {
    @Override
    public ComponentType getComponentType() {
        return ComponentType.TEXT;
    }

    @Override
    public TextComponentDetail makeSubComponent(TextComponentFactoryDto dto) {
        return dto.toComponentDetail();
    }
}
