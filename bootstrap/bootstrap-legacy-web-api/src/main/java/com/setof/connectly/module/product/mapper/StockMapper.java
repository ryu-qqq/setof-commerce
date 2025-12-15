package com.setof.connectly.module.product.mapper;

import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.product.entity.stock.StockReservation;

public interface StockMapper {
    StockReservation reservation(Order order);
}
