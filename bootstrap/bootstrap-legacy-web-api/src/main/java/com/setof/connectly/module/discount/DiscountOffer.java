package com.setof.connectly.module.discount;

import com.setof.connectly.module.product.entity.group.embedded.Price;

public interface DiscountOffer {

    long getSellerId();

    long getProductGroupId();

    Price getPrice();

    void setPrice(Price price);

    void setShareRatio(double shareRatio);
}
