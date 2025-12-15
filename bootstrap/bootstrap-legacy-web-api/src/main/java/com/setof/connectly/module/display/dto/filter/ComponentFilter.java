package com.setof.connectly.module.display.dto.filter;

import com.setof.connectly.module.common.filter.ItemFilter;
import com.setof.connectly.module.display.enums.component.ComponentType;
import java.util.List;

public interface ComponentFilter extends ItemFilter {

    ComponentType getComponentType();

    Long getTabId();

    void setExclusiveProductIds(List<Long> exclusiveProductIds);

    List<Long> getExclusiveProductIds();
}
