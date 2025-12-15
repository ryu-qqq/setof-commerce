package com.setof.connectly.module.discount.service.fetch;

import com.setof.connectly.module.discount.dto.DiscountCacheDto;
import com.setof.connectly.module.discount.dto.DiscountRedisFetchDto;
import java.util.List;
import java.util.Optional;

public interface DiscountFindService {

    Optional<DiscountCacheDto> fetchDiscountCache(DiscountRedisFetchDto discountRedisFetchDto);

    List<DiscountCacheDto> fetchDiscountCaches(List<Long> productGroupIds, List<Long> sellerIds);
}
