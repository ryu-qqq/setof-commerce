package com.setof.connectly.module.order.repository.snapshot.query.option;

import com.setof.connectly.module.order.entity.snapshot.option.OrderSnapShotOptionGroup;
import java.util.Set;

public interface OrderSnapShotOptionGroupJdbcRepository {

    void saveAll(Set<OrderSnapShotOptionGroup> orderSnapShotOptionGroups);
}
