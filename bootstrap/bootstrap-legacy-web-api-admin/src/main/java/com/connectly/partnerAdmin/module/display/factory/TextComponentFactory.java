package com.connectly.partnerAdmin.module.display.factory;


import com.connectly.partnerAdmin.module.display.dto.component.text.TextComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.factory.TextComponentFactoryDto;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import org.springframework.stereotype.Component;

@Component
public class TextComponentFactory implements ComponentFactory<TextComponentDetail, TextComponentFactoryDto> {
    @Override
    public ComponentType getComponentType() {
        return ComponentType.TEXT;
    }

    @Override
    public TextComponentDetail makeSubComponent(TextComponentFactoryDto dto) {
        return dto.toComponentDetail();
    }
}
