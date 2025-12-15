package com.setof.connectly.module.event.repository;

import com.setof.connectly.module.event.dto.EventProductStockCheck;
import java.util.List;
import java.util.Optional;

public interface EventProductFindRepository {

    List<EventProductStockCheck> fetchEventProducts(List<Long> productGroupIds);

    Optional<EventProductStockCheck> fetchEventProduct(long productGroupId);
}
