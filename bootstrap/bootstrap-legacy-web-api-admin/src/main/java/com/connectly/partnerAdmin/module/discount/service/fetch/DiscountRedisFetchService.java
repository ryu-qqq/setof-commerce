package com.connectly.partnerAdmin.module.discount.service.fetch;

import com.connectly.partnerAdmin.module.discount.core.BaseDiscountInfo;
import com.connectly.partnerAdmin.module.discount.core.PriceHolder;

import java.util.List;
import java.util.Optional;

public interface DiscountRedisFetchService {

    Optional<BaseDiscountInfo> getHighestPriorityDiscount(PriceHolder priceHolder);

    List<BaseDiscountInfo> getHighestPriorityDiscounts(List<? extends PriceHolder> priceHolders);

}
