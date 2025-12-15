package com.setof.connectly.module.common.filter;

import com.setof.connectly.module.display.enums.component.OrderType;

public interface LastDomainIdFilter {
    Long getLastDomainId();

    void setLastDomainId(Long lastDomainId);

    String getCursorValue();

    void setCursorValue(String cursorValue);

    OrderType getOrderType();

    void setOrderType(OrderType orderType);
}
