package com.setof.connectly.module.common.filter;

import com.setof.connectly.module.display.enums.component.OrderType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AbstractLastDomainIdFilter implements LastDomainIdFilter {

    private Long lastDomainId;
    private String cursorValue;

    private OrderType orderType;

    @Override
    public void setLastDomainId(Long lastDomainId) {
        this.lastDomainId = lastDomainId;
    }

    @Override
    public void setCursorValue(String cursorValue) {
        this.cursorValue = cursorValue;
    }

    @Override
    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }
}
