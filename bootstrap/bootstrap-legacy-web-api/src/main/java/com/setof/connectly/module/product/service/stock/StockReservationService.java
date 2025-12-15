package com.setof.connectly.module.product.service.stock;

import com.setof.connectly.module.order.entity.order.Order;
import java.util.List;

public interface StockReservationService {
    void stockReserve(Order order);

    void stocksReserve(List<Order> orders);

    void purchasedAll(long paymentId);

    void cancelsReserve(long paymentId);

    void cancelReserve(long orderId);
}
