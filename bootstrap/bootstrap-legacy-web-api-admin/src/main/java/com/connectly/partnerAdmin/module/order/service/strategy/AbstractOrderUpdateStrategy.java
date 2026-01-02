package com.connectly.partnerAdmin.module.order.service.strategy;


import com.connectly.partnerAdmin.module.order.dto.UpdateOrderResponse;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.exception.InvalidOrderStatusException;
import com.connectly.partnerAdmin.module.order.service.OrderFetchService;
import com.connectly.partnerAdmin.module.order.validator.OrderStatusProcessor;
import com.connectly.partnerAdmin.module.order.validator.OrderStatusTransition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public abstract class AbstractOrderUpdateStrategy<T extends UpdateOrder> implements OrderUpdateStrategy<T> {

    private final OrderStatusProcessor orderStatusProcessor;
    private final OrderFetchService orderFetchService;

    protected Order fetchOrderEntity(long orderId){
        return orderFetchService.fetchOrderEntity(orderId);
    }

    protected List<Order> fetchOrderEntities(List<Long> orderIds){
        return orderFetchService.fetchOrderEntities(orderIds);
    }

    @Override
    public UpdateOrderResponse updateOrder(T updateOrder) {
        Order order = fetchOrderEntity(updateOrder.getOrderId());
        OrderStatus pastOrderStatus = order.getOrderStatus();
        Order updatedOrder = updateOrderStatus(order, updateOrder);

        additionalUpdateLogic(updatedOrder, updateOrder);

        return toUpdateOrderResponse(pastOrderStatus, updatedOrder,  updateOrder.getChangeReason(), updateOrder.getChangeDetailReason());
    }


    @Override
    public List<UpdateOrderResponse> updateOrders(List<T> tList) {
        List<UpdateOrderResponse> results = new ArrayList<>();

        List<Long> orderIds = tList.stream().map(UpdateOrder::getOrderId).collect(Collectors.toList());
        if(!orderIds.isEmpty()){
            List<Order> orders = fetchOrderEntities(orderIds);
            Map<Long, Order> orderMap = orders.stream().collect(Collectors.toMap(Order::getId, Function.identity()));

            results = tList.stream()
                    .map(updateOrder -> {
                        Order order = orderMap.get(updateOrder.getOrderId());
                        OrderStatus pastOrderStatus = order.getOrderStatus();
                        Order updatedOrder = updateOrderStatus(order, updateOrder);

                        additionalUpdateLogic(updatedOrder, updateOrder);

                        return toUpdateOrderResponse(pastOrderStatus, updatedOrder, updateOrder.getChangeReason(), updateOrder.getChangeDetailReason());
                    }).collect(Collectors.toList());
        }
        return results;
    }


    protected Order updateOrderStatus(Order order, T updateOrder){
        validatedToOrderStatus(order, updateOrder);
        order.setOrderStatus(updateOrder.getOrderStatus(), updateOrder.getChangeReason(), updateOrder.getChangeDetailReason());
        return order;
    }


    protected UpdateOrderResponse toUpdateOrderResponse(OrderStatus pasOrderStatus, Order order, String changeReason, String changeDetailReason){
        return UpdateOrderResponse.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .asIsOrderStatus(pasOrderStatus)
                .toBeOrderStatus(order.getOrderStatus())
                .changeReason(changeReason)
                .changeDetailReason(changeDetailReason)
                .build();
    }

    private void validatedToOrderStatus(Order order, T updateOrder){
        if(updateOrder.isByPass()) return;

        OrderStatusTransition orderStatusTransition = orderStatusProcessor.get(order.getOrderStatus());

        if(orderStatusTransition == null){
            throw new InvalidOrderStatusException(order.getId(), order.getOrderStatus(), updateOrder.getOrderStatus());
        }

        boolean b = orderStatusTransition.canTransition(updateOrder.getOrderStatus());
        if(!b) throw new InvalidOrderStatusException(order.getId(), order.getOrderStatus(), updateOrder.getOrderStatus());

    }

    protected abstract void additionalUpdateLogic(Order order, T updateOrder);


}
