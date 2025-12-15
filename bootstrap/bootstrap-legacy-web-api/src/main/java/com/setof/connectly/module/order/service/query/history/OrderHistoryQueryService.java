package com.setof.connectly.module.order.service.query.history;

import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.order.entity.order.OrderHistory;
import java.util.List;

public interface OrderHistoryQueryService {

    void saveOrderHistory(Order order);

    void saveOrderHistories(List<Order> orders);

    void saveOrderHistoryEntities(List<OrderHistory> orderHistories);
}
