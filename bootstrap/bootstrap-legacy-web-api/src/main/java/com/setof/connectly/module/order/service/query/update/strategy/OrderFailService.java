package com.setof.connectly.module.order.service.query.update.strategy;

import com.setof.connectly.module.order.dto.fetch.UpdateOrderResponse;
import com.setof.connectly.module.order.dto.query.NormalOrder;
import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.order.mapper.OrderMapper;
import com.setof.connectly.module.order.service.fetch.OrderFindService;
import com.setof.connectly.module.order.service.status.OrderStatusProcessor;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderFailService extends AbstractOrderUpdateService<NormalOrder> {

    public OrderFailService(
            OrderFindService orderFindService,
            OrderMapper orderMapper,
            OrderStatusProcessor orderStatusProcessor) {
        super(orderFindService, orderMapper, orderStatusProcessor);
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.ORDER_FAILED;
    }

    @Override
    public List<UpdateOrderResponse> updateOrder(NormalOrder normalOrder) {
        Long paymentId = normalOrder.getPaymentId();
        List<Order> orders = fetchOrders(paymentId);

        return orders.stream()
                .map(order -> updateOrderStatus(order, OrderStatus.ORDER_FAILED))
                .map(order -> toUpdateOrderResponse(OrderStatus.ORDER_FAILED, order, "", ""))
                .collect(Collectors.toList());
    }
}
