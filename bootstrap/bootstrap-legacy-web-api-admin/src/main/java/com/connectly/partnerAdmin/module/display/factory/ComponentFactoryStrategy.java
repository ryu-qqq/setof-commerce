package com.connectly.partnerAdmin.module.display.factory;


import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.factory.ComponentFactoryMakeDto;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ComponentFactoryStrategy extends AbstractProvider<ComponentType, ComponentFactory<? extends SubComponent, ? extends ComponentFactoryMakeDto>> {

    public ComponentFactoryStrategy(List<ComponentFactory<? extends SubComponent, ? extends ComponentFactoryMakeDto>> factories) {
        for (ComponentFactory<? extends SubComponent, ? extends ComponentFactoryMakeDto> factory : factories) {
            map.put(factory.getComponentType(), factory);
        }
    }
}
