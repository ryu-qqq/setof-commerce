package com.setof.connectly.module.discount.service;

import com.setof.connectly.module.discount.dto.DiscountCacheDto;

public interface DiscountRedisQueryService {

    void saveByIssueTypeAndTargetId(long targetId, DiscountCacheDto discountCacheDto);
}
