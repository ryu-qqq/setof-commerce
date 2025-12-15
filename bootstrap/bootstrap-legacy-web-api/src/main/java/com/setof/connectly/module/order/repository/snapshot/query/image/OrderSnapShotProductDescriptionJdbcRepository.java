package com.setof.connectly.module.order.repository.snapshot.query.image;

import com.setof.connectly.module.order.entity.snapshot.image.OrderSnapShotProductGroupDetailDescription;
import java.util.Set;

public interface OrderSnapShotProductDescriptionJdbcRepository {

    void saveAll(
            Set<OrderSnapShotProductGroupDetailDescription>
                    orderSnapShotProductGroupDetailDescriptions);
}
