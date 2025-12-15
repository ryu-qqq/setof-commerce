package com.setof.connectly.module.display.factory;

import com.setof.connectly.module.display.dto.component.tab.TabComponentDetail;
import com.setof.connectly.module.display.dto.factory.TabComponentFactoryDto;
import com.setof.connectly.module.display.enums.component.ComponentType;
import org.springframework.stereotype.Component;

@Component
public class TabComponentFactory
        implements ComponentFactory<TabComponentDetail, TabComponentFactoryDto> {
    @Override
    public ComponentType getComponentType() {
        return ComponentType.TAB;
    }

    @Override
    public TabComponentDetail makeSubComponent(TabComponentFactoryDto dto) {
        return dto.toComponentDetail();
    }
}
