package com.setof.connectly.module.order.repository.snapshot.query.group;

import com.setof.connectly.module.order.entity.snapshot.group.OrderSnapShotProductGroup;
import java.util.Set;

public interface OrderSnapShotProductGroupJdbcRepository {

    void saveAll(Set<OrderSnapShotProductGroup> orderSnapShotProductGroups);
}
