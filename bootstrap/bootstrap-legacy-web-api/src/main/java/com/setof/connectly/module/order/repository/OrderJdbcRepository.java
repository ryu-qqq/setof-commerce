package com.setof.connectly.module.order.repository;

import com.setof.connectly.module.order.enums.OrderStatus;

public interface OrderJdbcRepository {

    void updateOrderStatus(long paymentId, OrderStatus orderStatus);
}
