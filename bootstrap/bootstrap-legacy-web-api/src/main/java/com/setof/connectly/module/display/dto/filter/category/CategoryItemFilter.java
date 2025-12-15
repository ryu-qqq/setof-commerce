package com.setof.connectly.module.display.dto.filter.category;

import com.setof.connectly.module.display.dto.filter.AbstractSortItemFilter;
import com.setof.connectly.module.display.enums.component.ComponentType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class CategoryItemFilter extends AbstractSortItemFilter {

    @Override
    public ComponentType getComponentType() {
        return ComponentType.CATEGORY;
    }
}
