package com.setof.connectly.module.display.repository.component.item.fetch;

import com.setof.connectly.module.display.entity.component.ComponentTarget;
import com.setof.connectly.module.display.enums.SortType;
import java.util.Optional;

public interface ComponentTargetFindRepository {

    Optional<ComponentTarget> fetchComponentTarget(long componentId, SortType sortType, Long tabId);
}
