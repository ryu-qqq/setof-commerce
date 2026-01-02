package com.connectly.partnerAdmin.module.display.dto.query.tab;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQuery;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.enums.OrderType;
import com.connectly.partnerAdmin.module.display.enums.TabMovingType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TabComponentQueryDto implements ComponentQuery {
    private long componentId;
    private long tabComponentId;
    private long tabId;
    private String tabName;
    private Yn stickyYn;
    private TabMovingType tabMovingType;
    private int displayOrder;
    private OrderType orderType;


    @QueryProjection
    public TabComponentQueryDto(long componentId, long tabComponentId, long tabId, String tabName, Yn stickyYn, TabMovingType tabMovingType, int displayOrder, OrderType orderType) {
        this.componentId = componentId;
        this.tabComponentId = tabComponentId;
        this.tabId = tabId;
        this.tabName = tabName;
        this.stickyYn = stickyYn;
        this.tabMovingType = tabMovingType;
        this.displayOrder = displayOrder;
        this.orderType = orderType;
    }

    @Override
    public int hashCode() {
        return String.valueOf(this.componentId + this.tabComponentId+ this.tabId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TabComponentQueryDto p) {
            return this.hashCode()==p.hashCode();
        }
        return false;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TAB;
    }
}
