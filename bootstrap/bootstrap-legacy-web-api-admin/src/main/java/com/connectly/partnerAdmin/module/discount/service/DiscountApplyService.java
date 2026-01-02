package com.connectly.partnerAdmin.module.discount.service;


import com.connectly.partnerAdmin.module.discount.core.PriceHolder;

import java.util.List;

public interface DiscountApplyService {

    void applyDiscount(PriceHolder priceHolder);

    void applyDiscount(List<? extends PriceHolder> priceHolders);

}
