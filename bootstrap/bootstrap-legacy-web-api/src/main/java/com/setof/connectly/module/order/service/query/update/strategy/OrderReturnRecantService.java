package com.setof.connectly.module.order.service.query.update.strategy;

import com.setof.connectly.module.order.dto.fetch.UpdateOrderResponse;
import com.setof.connectly.module.order.dto.query.NormalOrder;
import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.order.mapper.OrderMapper;
import com.setof.connectly.module.order.service.fetch.OrderFindService;
import com.setof.connectly.module.order.service.status.OrderStatusProcessor;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderReturnRecantService extends AbstractOrderUpdateService<NormalOrder> {

    public OrderReturnRecantService(
            OrderFindService orderFindService,
            OrderMapper orderMapper,
            OrderStatusProcessor orderStatusProcessor) {
        super(orderFindService, orderMapper, orderStatusProcessor);
    }

    @Override
    public List<UpdateOrderResponse> updateOrder(NormalOrder normalOrder) {
        Order order = fetchOrder(normalOrder.getOrderId());
        normalOrder.setOrderStatus(OrderStatus.DELIVERY_COMPLETED);
        updateOrderStatus(order, normalOrder.getOrderStatus());
        UpdateOrderResponse updateOrderResponse =
                toUpdateOrderResponse(normalOrder.getOrderStatus(), order, "", "");
        return Collections.singletonList(updateOrderResponse);
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.RETURN_REQUEST_RECANT;
    }
}
