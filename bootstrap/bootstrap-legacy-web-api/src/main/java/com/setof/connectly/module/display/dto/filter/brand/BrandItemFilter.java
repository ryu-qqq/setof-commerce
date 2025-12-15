package com.setof.connectly.module.display.dto.filter.brand;

import com.setof.connectly.module.display.dto.filter.AbstractSortItemFilter;
import com.setof.connectly.module.display.enums.component.ComponentType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class BrandItemFilter extends AbstractSortItemFilter {

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BRAND;
    }
}
