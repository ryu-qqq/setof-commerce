package com.connectly.partnerAdmin.module.display.dto.query.brand;

import com.connectly.partnerAdmin.module.brand.core.BaseBrandContext;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQuery;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.enums.OrderType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandComponentQueryDto implements ComponentQuery {
    private long componentId;
    private long brandComponentId;
    private long brandComponentItemId;
    private BaseBrandContext brand;
    private long categoryId;
    private OrderType orderType;


    @QueryProjection
    public BrandComponentQueryDto(long componentId, long brandComponentId, long brandComponentItemId, BaseBrandContext brand, long categoryId, OrderType orderType) {
        this.componentId = componentId;
        this.brandComponentId = brandComponentId;
        this.brandComponentItemId = brandComponentItemId;
        this.brand = brand;
        this.categoryId = categoryId;
        this.orderType = orderType;
    }


    @Override
    public int hashCode() {
        return String.valueOf(this.componentId + this.brandComponentId + this.brandComponentItemId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BrandComponentQueryDto p) {
            return this.hashCode()==p.hashCode();
        }
        return false;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BRAND;
    }
}
