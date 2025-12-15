package com.setof.connectly.module.order.repository.snapshot.query.option;

import com.setof.connectly.module.order.entity.snapshot.option.OrderSnapShotProductOption;
import java.util.Set;

public interface OrderSnapShotProductOptionJdbcRepository {

    void saveAll(Set<OrderSnapShotProductOption> orderSnapShotProductOptions);
}
