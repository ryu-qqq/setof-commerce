package com.setof.connectly.module.common.mapper;

import java.time.LocalDateTime;

public interface CursorValueProvider extends LastDomainIdProvider {

    double getScore();

    double getAverageRating();

    long getSalePrice();

    int getDiscountRate();

    long getReviewCount();

    LocalDateTime getInsertDate();
}
