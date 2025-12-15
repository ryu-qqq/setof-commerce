package com.setof.connectly.module.order.repository.snapshot.query.individual;

import com.setof.connectly.module.order.entity.snapshot.group.OrderSnapShotProduct;
import java.util.Set;

public interface OrderSnapShotProductJdbcRepository {

    void saveAll(Set<OrderSnapShotProduct> orderSnapShotProducts);
}
