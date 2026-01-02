package com.connectly.partnerAdmin.module.display.repository.component.item.fetch;

import com.connectly.partnerAdmin.module.display.entity.component.ComponentTarget;
import com.connectly.partnerAdmin.module.display.enums.SortType;

import java.util.Optional;

public interface ComponentTargetFetchRepository {

    Optional<ComponentTarget> fetchComponentTarget(long componentId, SortType sortType, Long tabId);
}
