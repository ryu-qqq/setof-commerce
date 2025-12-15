package com.setof.connectly.module.discount.service.fetch;

import com.setof.connectly.module.discount.dto.DiscountCacheDto;
import com.setof.connectly.module.discount.dto.DiscountRedisFetchDto;
import java.util.List;
import java.util.Optional;

public interface DiscountRedisFindService {

    Optional<DiscountCacheDto> getHighestPriorityDiscount(
            DiscountRedisFetchDto discountRedisFetchDto);

    List<DiscountCacheDto> getHighestPriorityDiscounts(
            List<DiscountRedisFetchDto> discountRedisFetchDtos);
}
