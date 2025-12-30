package com.connectly.partnerAdmin.module.common.filter;


import com.connectly.partnerAdmin.module.product.entity.group.embedded.Price;

import java.time.LocalDateTime;

public interface CursorValueProvider extends LastDomainIdProvider {

    double getScore();
    double getAverageRating();
    Price getPrice();
    long getReviewCount();
    LocalDateTime getInsertDate();


    default double getSalePrice() {
        return getPrice().getSalePrice().doubleValue();
    }

    default double getDiscountRate() {
        return getPrice().getDiscountRate();
    }

}
