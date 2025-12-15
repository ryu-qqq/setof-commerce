package com.setof.connectly.module.order.service.query;

import com.setof.connectly.module.order.dto.order.CreateOrder;
import com.setof.connectly.module.order.dto.order.OrderSheet;
import com.setof.connectly.module.order.entity.order.Order;
import java.util.List;

public interface OrderCreateService {

    Order issueOrder(CreateOrder order);

    List<Order> issueOrders(List<? extends OrderSheet> orders);
}
