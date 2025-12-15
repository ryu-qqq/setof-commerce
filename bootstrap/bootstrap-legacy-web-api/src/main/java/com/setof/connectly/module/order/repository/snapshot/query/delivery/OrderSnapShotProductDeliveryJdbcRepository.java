package com.setof.connectly.module.order.repository.snapshot.query.delivery;

import com.setof.connectly.module.order.entity.snapshot.delivery.OrderSnapShotProductDelivery;
import java.util.Set;

public interface OrderSnapShotProductDeliveryJdbcRepository {

    void saveAll(Set<OrderSnapShotProductDelivery> orderSnapShotProductDeliveries);
}
