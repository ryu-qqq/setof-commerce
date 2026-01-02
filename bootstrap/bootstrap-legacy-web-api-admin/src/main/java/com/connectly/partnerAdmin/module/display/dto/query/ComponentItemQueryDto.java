package com.connectly.partnerAdmin.module.display.dto.query;

import com.connectly.partnerAdmin.module.brand.core.BaseBrandContext;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.enums.OrderType;
import com.connectly.partnerAdmin.module.display.enums.SortType;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.Price;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ComponentItemQueryDto {

    private long componentId;
    private long componentTargetId;
    private long componentItemId;
    private long productGroupId;
    private BaseBrandContext brand;
    private String productDisplayName;
    private String productImageUrl;
    private Price price;
    private int displayOrder;
    private ComponentType componentType;
    private long tabId;
    private SortType sortType;
    private OrderType orderType;

    @QueryProjection
    public ComponentItemQueryDto(long componentId, long componentTargetId, long componentItemId, long productGroupId, BaseBrandContext brand, String productDisplayName, String productImageUrl, Price price, int displayOrder, ComponentType componentType, long tabId, SortType sortType, OrderType orderType) {
        this.componentId = componentId;
        this.componentTargetId = componentTargetId;
        this.componentItemId = componentItemId;
        this.productGroupId = productGroupId;
        this.brand = brand;
        this.productDisplayName = productDisplayName;
        this.productImageUrl = productImageUrl;
        this.price = price;
        this.displayOrder = displayOrder;
        this.componentType = componentType;
        this.tabId = tabId;
        this.sortType = sortType;
        this.orderType = orderType;
    }

    @Override
    public int hashCode() {
        return String.valueOf( this.componentItemId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ComponentItemQueryDto p) {
            return this.hashCode()==p.hashCode();
        }
        return false;
    }
}
