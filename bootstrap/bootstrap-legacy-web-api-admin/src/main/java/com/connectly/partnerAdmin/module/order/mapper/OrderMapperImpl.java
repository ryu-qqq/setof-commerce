package com.connectly.partnerAdmin.module.order.mapper;


import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.order.dto.OrderSnapShotQueryDto;
import com.connectly.partnerAdmin.module.order.dto.query.OrderSheet;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.payment.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapperImpl implements OrderMapper {

    private final OrderSnapShotMapper orderSnapShotMapper;


    @Override
    public Order toOrder(Payment payment, OrderSheet createOrder, OrderSnapShotQueryDto orderSnapShotQueryDto) {
        Money orderAmount = createOrder.getOrderAmount().times(createOrder.getQuantity());
        Order order = new Order(orderAmount, OrderStatus.ORDER_COMPLETED, createOrder.getQuantity(), Yn.N, createOrder.getProductId(), createOrder.getUserId(), createOrder.getSellerId());
        payment.addOrder(order);
        orderSnapShotMapper.setSnapShot(order, orderSnapShotQueryDto);

        return order;
    }

}
