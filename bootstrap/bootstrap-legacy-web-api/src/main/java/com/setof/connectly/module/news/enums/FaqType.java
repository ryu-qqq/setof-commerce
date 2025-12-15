package com.setof.connectly.module.news.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FaqType {
    MEMBER_LOGIN,
    PRODUCT_SELLER,
    SHIPPING,
    ORDER_PAYMENT,
    CANCEL_REFUND,
    EXCHANGE_RETURN,
    TOP;

    public boolean isTop() {
        return this.equals(TOP);
    }
}
