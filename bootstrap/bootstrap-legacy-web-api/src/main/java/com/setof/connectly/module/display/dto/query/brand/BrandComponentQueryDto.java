package com.setof.connectly.module.display.dto.query.brand;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.display.dto.query.ComponentQuery;
import com.setof.connectly.module.display.enums.component.ComponentType;
import com.setof.connectly.module.display.enums.component.OrderType;
import com.setof.connectly.module.product.dto.brand.BrandDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandComponentQueryDto implements ComponentQuery {
    private long componentId;
    private long brandComponentId;
    private BrandDto brand;
    private long categoryId;
    private OrderType orderType;

    @QueryProjection
    public BrandComponentQueryDto(
            long componentId,
            long brandComponentId,
            BrandDto brand,
            long categoryId,
            OrderType orderType) {
        this.componentId = componentId;
        this.brandComponentId = brandComponentId;
        this.brand = brand;
        this.categoryId = categoryId;
        this.orderType = orderType;
    }

    @Override
    public int hashCode() {
        return String.valueOf(this.componentId + this.brandComponentId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BrandComponentQueryDto) {
            BrandComponentQueryDto p = (BrandComponentQueryDto) obj;
            return this.hashCode() == p.hashCode();
        }
        return false;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BRAND;
    }
}
