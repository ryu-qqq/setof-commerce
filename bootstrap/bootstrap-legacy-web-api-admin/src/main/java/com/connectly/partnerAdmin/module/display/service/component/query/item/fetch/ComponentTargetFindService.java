package com.connectly.partnerAdmin.module.display.service.component.query.item.fetch;

import com.connectly.partnerAdmin.module.display.entity.component.ComponentTarget;
import com.connectly.partnerAdmin.module.display.enums.SortType;

import java.util.Optional;

public interface ComponentTargetFindService {

    Optional<ComponentTarget> fetchComponentTarget(long componentId, SortType sortType, Long tabId);

}
