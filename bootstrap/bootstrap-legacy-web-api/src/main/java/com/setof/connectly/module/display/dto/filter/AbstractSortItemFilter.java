package com.setof.connectly.module.display.dto.filter;

import com.setof.connectly.module.common.filter.AbstractItemFilter;
import com.setof.connectly.module.display.enums.component.ComponentType;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public abstract class AbstractSortItemFilter extends AbstractItemFilter implements ComponentFilter {

    private ComponentType componentType;
    private List<Long> exclusiveProductIds;

    @Override
    public Long getTabId() {
        return null;
    }

    @Override
    public void setExclusiveProductIds(List<Long> exclusiveProductIds) {
        this.exclusiveProductIds = exclusiveProductIds;
    }
}
