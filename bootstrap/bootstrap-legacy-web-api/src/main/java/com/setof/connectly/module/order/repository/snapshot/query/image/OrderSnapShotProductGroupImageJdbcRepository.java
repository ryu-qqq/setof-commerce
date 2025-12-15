package com.setof.connectly.module.order.repository.snapshot.query.image;

import com.setof.connectly.module.order.entity.snapshot.image.OrderSnapShotProductGroupImage;
import java.util.Set;

public interface OrderSnapShotProductGroupImageJdbcRepository {

    void saveAll(Set<OrderSnapShotProductGroupImage> orderSnapShotProductGroupImages);
}
