package com.setof.connectly.module.display.dto.filter.product;

import com.setof.connectly.module.display.dto.filter.AbstractSortItemFilter;
import com.setof.connectly.module.display.enums.component.ComponentType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class ProductItemFilter extends AbstractSortItemFilter {

    @Override
    public ComponentType getComponentType() {
        return ComponentType.PRODUCT;
    }
}
