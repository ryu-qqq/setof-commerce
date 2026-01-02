package com.connectly.partnerAdmin.module.display.mapper.item;

import com.connectly.partnerAdmin.module.display.entity.component.ComponentTarget;
import com.connectly.partnerAdmin.module.display.enums.SortType;

public interface ComponentTargetMapper {

    ComponentTarget toEntity(long componentId, SortType sortType, Long tabId);

}
