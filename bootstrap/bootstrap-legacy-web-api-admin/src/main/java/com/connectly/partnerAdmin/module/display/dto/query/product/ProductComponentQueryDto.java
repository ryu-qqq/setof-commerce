package com.connectly.partnerAdmin.module.display.dto.query.product;

import com.connectly.partnerAdmin.module.display.dto.query.ComponentQuery;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.enums.OrderType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductComponentQueryDto implements ComponentQuery {

    private long componentId;
    private long productComponentId;
    private OrderType orderType;

    @QueryProjection
    public ProductComponentQueryDto(long componentId, long productComponentId, OrderType orderType) {
        this.componentId = componentId;
        this.productComponentId = productComponentId;
        this.orderType = orderType;
    }

    @Override
    public int hashCode() {
        return String.valueOf(this.componentId + this.productComponentId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ProductComponentQueryDto p) {
            return this.hashCode()==p.hashCode();
        }
        return false;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.PRODUCT;
    }
}
