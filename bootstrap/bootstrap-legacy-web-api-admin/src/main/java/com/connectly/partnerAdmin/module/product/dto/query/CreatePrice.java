package com.connectly.partnerAdmin.module.product.dto.query;

import com.connectly.partnerAdmin.module.generic.money.Money;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreatePrice {

    //@Min(value = 100, message = "Product Regular Price must be at least 100.")
    private Money regularPrice;

    //@Min(value = 100, message = "Product Current Price must be at least 100.")
    private Money currentPrice;

}
