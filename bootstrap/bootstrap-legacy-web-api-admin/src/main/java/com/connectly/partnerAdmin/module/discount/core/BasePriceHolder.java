package com.connectly.partnerAdmin.module.discount.core;

import com.connectly.partnerAdmin.module.product.entity.group.embedded.Price;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class BasePriceHolder implements PriceHolder{

    @Setter
    private Price price;
    private long productGroupId;
    private long sellerId;

}

