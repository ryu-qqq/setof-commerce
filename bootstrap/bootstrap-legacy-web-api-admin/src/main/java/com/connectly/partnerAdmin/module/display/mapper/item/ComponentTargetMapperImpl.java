package com.connectly.partnerAdmin.module.display.mapper.item;

import com.connectly.partnerAdmin.module.display.entity.component.ComponentTarget;
import com.connectly.partnerAdmin.module.display.enums.SortType;
import org.springframework.stereotype.Component;

@Component
public class ComponentTargetMapperImpl implements ComponentTargetMapper{


    @Override
    public ComponentTarget toEntity(long componentId, SortType sortType, Long tabId) {
        return ComponentTarget.builder()
                .componentId(componentId)
                .sortType(sortType)
                .tabId(tabId != null ? tabId : 0L)
                .build();
    }
}
