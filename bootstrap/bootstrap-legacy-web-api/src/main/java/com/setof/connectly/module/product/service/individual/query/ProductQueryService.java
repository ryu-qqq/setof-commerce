package com.setof.connectly.module.product.service.individual.query;

import java.util.List;

public interface ProductQueryService {
    void updatesStatus(List<Long> productIds);

    void rollBackUpdatesStatus(List<Long> productIds);
}
