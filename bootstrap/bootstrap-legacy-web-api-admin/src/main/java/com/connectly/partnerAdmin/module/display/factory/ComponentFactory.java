package com.connectly.partnerAdmin.module.display.factory;

import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.factory.ComponentFactoryMakeDto;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;

public interface ComponentFactory<T extends SubComponent, R extends ComponentFactoryMakeDto> {

    ComponentType getComponentType();
    T makeSubComponent(R componentFactoryMakeDto);
}
