package com.connectly.partnerAdmin.module.display.dto.query;


import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.enums.OrderType;

public interface ComponentQuery {
    long getComponentId();
    ComponentType getComponentType();

}
