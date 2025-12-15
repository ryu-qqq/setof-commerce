package com.setof.connectly.module.display.dto.filter.tab;

import com.setof.connectly.module.display.dto.filter.AbstractSortItemFilter;
import com.setof.connectly.module.display.enums.component.ComponentType;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class TabProductFilter extends AbstractSortItemFilter {

    private long tabId;

    @Override
    public Long getTabId() {
        return tabId;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TAB;
    }
}
