package com.connectly.partnerAdmin.module.display.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import com.querydsl.core.types.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;

@Getter
@AllArgsConstructor
public enum OrderType implements EnumType {
    NONE("", ASC),
    RECOMMEND("score", DESC),
    REVIEW("reviewCount", DESC),
    RECENT("updateDate", ASC),
    LOW_PRICE("productGroupDetails.price.salePrice", ASC),
    HIGH_PRICE("productGroupDetails.price.salePrice", DESC),
    LOW_DISCOUNT("productGroupDetails.price.discountRate", ASC),
    HIGH_DISCOUNT("productGroupDetails.price.discountRate", DESC);


    private final String field;
    private final Order direction;
    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}
