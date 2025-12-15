package com.setof.connectly.module.order.service.query.update.strategy;

import com.setof.connectly.module.exception.event.EventProductRefundException;
import com.setof.connectly.module.exception.order.ForbiddenOrderStatusException;
import com.setof.connectly.module.exception.order.InvalidOrderStatusException;
import com.setof.connectly.module.order.dto.fetch.OrderHistoryResponse;
import com.setof.connectly.module.order.dto.fetch.UpdateOrderResponse;
import com.setof.connectly.module.order.dto.query.UpdateOrder;
import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.order.mapper.OrderMapper;
import com.setof.connectly.module.order.service.fetch.OrderFindService;
import com.setof.connectly.module.order.service.query.update.OrderUpdateService;
import com.setof.connectly.module.order.service.status.OrderStatusProcessor;
import com.setof.connectly.module.order.service.status.OrderStatusTransition;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public abstract class AbstractOrderUpdateService<T extends UpdateOrder>
        implements OrderUpdateService<T> {

    private final OrderFindService orderFindService;
    private final OrderMapper orderMapper;
    private final OrderStatusProcessor orderStatusProcessor;

    @Override
    public List<UpdateOrderResponse> updateOrder(T t) {
        Order order = fetchOrder(t.getOrderId());
        OrderStatus pastOrderStatus = order.getOrderStatus();
        Order updatedOrder = updateOrderStatus(order, t.getOrderStatus());
        return Collections.singletonList(
                toUpdateOrderResponse(
                        pastOrderStatus,
                        updatedOrder,
                        t.getChangeReason(),
                        t.getChangeDetailReason()));
    }

    protected Order updateOrderStatus(Order order, OrderStatus orderStatus) {
        validatedToOrderStatus(order, orderStatus);
        order.updateOrderStatus(orderStatus);
        return order;
    }

    protected List<Order> fetchOrders(long paymentId) {
        return orderFindService.fetchOrderEntities(paymentId);
    }

    protected Order fetchOrder(long orderId) {
        return orderFindService.fetchOrderEntity(orderId);
    }

    protected Optional<OrderHistoryResponse> fetchOrderHistory(
            long orderId, OrderStatus orderStatus) {
        return orderFindService.fetchOrderHistory(orderId, orderStatus);
    }

    protected UpdateOrderResponse toUpdateOrderResponse(
            OrderStatus pastOrderStatus,
            Order order,
            String changeReason,
            String changeDetailReason) {
        return orderMapper.toUpdateOrderResponse(
                pastOrderStatus, order, changeReason, changeDetailReason);
    }

    private void validatedToOrderStatus(Order order, OrderStatus toBeOrderStatus) {

        OrderStatusTransition orderStatusTransition = orderStatusProcessor.get(toBeOrderStatus);
        if (orderStatusTransition == null) {
            throw new ForbiddenOrderStatusException(
                    order.getId(), order.getOrderStatus(), toBeOrderStatus);
        }

        boolean b = orderStatusTransition.canTransition(order.getOrderStatus());
        if (!b)
            throw new InvalidOrderStatusException(
                    order.getId(), order.getOrderStatus(), toBeOrderStatus);
    }

    protected boolean fetchIsRaffleOrderProduct(long orderId) {
        return orderFindService.isRaffleOrderProduct(orderId);
    }

    protected void checkRaffleOrderProduct(long orderId) {
        boolean raffleOrderProduct = fetchIsRaffleOrderProduct(orderId);
        if (raffleOrderProduct) throw new EventProductRefundException();
    }
}
