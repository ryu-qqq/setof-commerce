package com.setof.connectly.module.event.service.product;

import java.util.List;

public interface EventProductRedisFindService {

    List<String> fetchEventProductStockCheck(List<Long> productGroupIds);

    String fetchEventProduct(long productGroupId);
}
