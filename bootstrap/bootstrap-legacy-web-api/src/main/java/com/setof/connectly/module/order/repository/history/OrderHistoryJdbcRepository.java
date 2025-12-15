package com.setof.connectly.module.order.repository.history;

import com.setof.connectly.module.order.entity.order.OrderHistory;
import java.util.List;

public interface OrderHistoryJdbcRepository {

    void saveAll(List<OrderHistory> orderHistories);
}
