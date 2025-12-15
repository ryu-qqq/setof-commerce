package com.setof.connectly.module.order.repository.snapshot.query.mileage;

import com.setof.connectly.module.order.entity.snapshot.mileage.OrderSnapShotMileageDetail;
import java.util.List;

public interface OrderSnapShotMileageDetailJdbcRepository {

    void saveAll(List<OrderSnapShotMileageDetail> orderSnapShotMileageDetails);
}
