package com.setof.connectly.module.event.service.product;

import com.setof.connectly.module.event.dto.EventProductGroup;

public interface EventProductRedisQueryService {

    void saveEventProductStockCheckInCache(EventProductGroup eventProductGroup);
}
