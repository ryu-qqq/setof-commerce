package com.setof.connectly.module.event.service.product;

import com.setof.connectly.module.event.dto.EventProductStockCheck;
import com.setof.connectly.module.event.enums.EventProductType;
import java.util.List;

public interface EventProductFindService {
    List<EventProductStockCheck> fetchEventProductStockCheck(List<Long> productGroupIds);

    EventProductType fetchEventProductType(long productGroupId);
}
