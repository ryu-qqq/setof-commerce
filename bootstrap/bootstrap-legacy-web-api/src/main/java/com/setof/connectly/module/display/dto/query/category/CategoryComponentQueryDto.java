package com.setof.connectly.module.display.dto.query.category;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.display.dto.query.ComponentQuery;
import com.setof.connectly.module.display.enums.component.ComponentType;
import com.setof.connectly.module.display.enums.component.OrderType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryComponentQueryDto implements ComponentQuery {
    private long componentId;
    private long categoryComponentId;
    private long categoryId;

    private OrderType orderType;

    @QueryProjection
    public CategoryComponentQueryDto(
            long componentId, long categoryComponentId, long categoryId, OrderType orderType) {
        this.componentId = componentId;
        this.categoryComponentId = categoryComponentId;
        this.categoryId = categoryId;
        this.orderType = orderType;
    }

    @Override
    public int hashCode() {
        return String.valueOf(this.componentId + this.categoryComponentId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CategoryComponentQueryDto) {
            CategoryComponentQueryDto p = (CategoryComponentQueryDto) obj;
            return this.hashCode() == p.hashCode();
        }
        return false;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.CATEGORY;
    }
}
