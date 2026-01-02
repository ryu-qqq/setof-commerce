package com.connectly.partnerAdmin.module.display.dto.query.category;

import com.connectly.partnerAdmin.module.display.dto.query.ComponentQuery;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.enums.OrderType;
import com.querydsl.core.annotations.QueryProjection;
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
    public CategoryComponentQueryDto(long componentId, long categoryComponentId, long categoryId, OrderType orderType) {
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
        if(obj instanceof CategoryComponentQueryDto p) {
            return this.hashCode()==p.hashCode();
        }
        return false;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.CATEGORY;
    }
}
