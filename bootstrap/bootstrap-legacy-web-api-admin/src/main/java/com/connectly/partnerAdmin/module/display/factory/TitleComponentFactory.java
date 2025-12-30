package com.connectly.partnerAdmin.module.display.factory;

import com.connectly.partnerAdmin.module.display.dto.component.title.TitleComponentDetail;

import com.connectly.partnerAdmin.module.display.dto.factory.TitleComponentFactoryDto;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import org.springframework.stereotype.Component;

@Component
public class TitleComponentFactory implements ComponentFactory<TitleComponentDetail, TitleComponentFactoryDto>{
    @Override
    public ComponentType getComponentType() {
        return ComponentType.TITLE;
    }

    @Override
    public TitleComponentDetail makeSubComponent(TitleComponentFactoryDto dto) {
        return dto.toComponentDetail();
    }


}
