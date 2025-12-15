package com.setof.connectly.module.display.dto.query;

import com.setof.connectly.module.display.enums.component.ComponentType;

public interface ComponentQuery {
    long getComponentId();

    ComponentType getComponentType();
}
