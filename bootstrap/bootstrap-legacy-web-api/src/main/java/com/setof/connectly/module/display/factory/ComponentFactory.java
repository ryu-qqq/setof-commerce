package com.setof.connectly.module.display.factory;

import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.factory.ComponentFactoryMakeDto;
import com.setof.connectly.module.display.enums.component.ComponentType;

public interface ComponentFactory<T extends SubComponent, R extends ComponentFactoryMakeDto> {

    ComponentType getComponentType();

    T makeSubComponent(R componentFactoryMakeDto);
}
