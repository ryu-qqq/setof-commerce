package com.setof.connectly.module.cart.dto;

import com.setof.connectly.module.common.filter.AbstractLastDomainIdFilter;
import com.setof.connectly.module.display.enums.component.OrderType;

public class CartFilter extends AbstractLastDomainIdFilter {

    public CartFilter(Long lastDomainId, String cursorValue, OrderType orderType) {
        super(lastDomainId, cursorValue, orderType);
    }

    @Override
    public Long getLastDomainId() {
        return super.getLastDomainId();
    }

    @Override
    public String getCursorValue() {
        return super.getCursorValue();
    }

    @Override
    public OrderType getOrderType() {
        return super.getOrderType();
    }

    @Override
    public void setLastDomainId(Long lastDomainId) {
        super.setLastDomainId(lastDomainId);
    }

    @Override
    public void setCursorValue(String cursorValue) {
        super.setCursorValue(cursorValue);
    }
}
