package com.setof.connectly.module.discount.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DiscountUseDto {

    private long userId;
    private String name;
    private long paymentId;
    private long orderId;
    private long productGroupId;
    private long sellerId;
}
