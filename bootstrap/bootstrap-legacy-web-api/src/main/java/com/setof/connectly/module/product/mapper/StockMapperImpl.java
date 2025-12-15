package com.setof.connectly.module.product.mapper;

import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.product.entity.stock.StockReservation;
import com.setof.connectly.module.product.enums.stock.ReservationStatus;
import com.setof.connectly.module.utils.SecurityUtils;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class StockMapperImpl implements StockMapper {

    public StockReservation reservation(Order order) {
        return StockReservation.builder()
                .orderId(order.getId())
                .paymentId(order.getPaymentId())
                .productId(order.getProductId())
                .stockQuantity(order.getQuantity())
                .userId(SecurityUtils.currentUserId())
                .reservedAt(LocalDateTime.now())
                .reservationStatus(ReservationStatus.RESERVED)
                .build();
    }
}
