package com.setof.connectly.module.order.repository.snapshot.query.stock;

import com.setof.connectly.module.order.entity.snapshot.stock.OrderSnapShotProductStock;
import java.util.Set;

public interface OrderSnapShotProductStockJdbcRepository {

    void saveAll(Set<OrderSnapShotProductStock> orderSnapShotProductStocks);
}
