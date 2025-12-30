package com.connectly.partnerAdmin.module.discount.core;

import com.connectly.partnerAdmin.module.product.entity.group.embedded.Price;

public interface PriceHolder {
    Price getPrice();
    void setPrice(Price price);
    long getProductGroupId();
    long getSellerId();
}
