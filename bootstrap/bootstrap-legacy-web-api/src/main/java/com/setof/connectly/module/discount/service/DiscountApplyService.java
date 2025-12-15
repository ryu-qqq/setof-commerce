package com.setof.connectly.module.discount.service;

import com.setof.connectly.module.discount.DiscountOffer;
import java.util.Collection;

public interface DiscountApplyService {
    <T extends DiscountOffer> void applyDiscountOffer(T discountOffer);

    void applyDiscountsOffer(Collection<? extends DiscountOffer> discountOffers);
}
