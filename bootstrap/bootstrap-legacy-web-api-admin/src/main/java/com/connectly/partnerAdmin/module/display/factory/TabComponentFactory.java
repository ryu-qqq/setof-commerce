package com.connectly.partnerAdmin.module.display.factory;

import com.connectly.partnerAdmin.module.display.dto.component.tab.TabComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.factory.TabComponentFactoryDto;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import org.springframework.stereotype.Component;

@Component
public class TabComponentFactory implements ComponentFactory<TabComponentDetail, TabComponentFactoryDto>{
    @Override
    public ComponentType getComponentType() {
        return ComponentType.TAB;
    }
    @Override
    public TabComponentDetail makeSubComponent(TabComponentFactoryDto dto) {
        return dto.toComponentDetail();
    }
}
