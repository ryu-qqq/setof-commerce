package com.setof.connectly.module.product.repository.individual;

import java.util.List;

public interface ProductJdbcRepository {

    void updateProductGroupSoldOutStatus(List<Long> productGroupIds);

    void updateProductGroupAvailableStatus(List<Long> productGroupIds);
}
