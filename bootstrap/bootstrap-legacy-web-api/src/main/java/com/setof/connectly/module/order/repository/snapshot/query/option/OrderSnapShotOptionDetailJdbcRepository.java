package com.setof.connectly.module.order.repository.snapshot.query.option;

import com.setof.connectly.module.order.entity.snapshot.option.OrderSnapShotOptionDetail;
import java.util.Set;

public interface OrderSnapShotOptionDetailJdbcRepository {
    void saveAll(Set<OrderSnapShotOptionDetail> orderSnapShotOptionDetails);
}
