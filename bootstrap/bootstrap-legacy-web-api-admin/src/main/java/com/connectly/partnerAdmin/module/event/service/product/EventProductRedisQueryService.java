package com.connectly.partnerAdmin.module.event.service.product;

import com.connectly.partnerAdmin.module.event.dto.EventProductGroup;

public interface EventProductRedisQueryService {

    void saveEventProductStockCheckInCache(EventProductGroup eventProductGroup);

}
