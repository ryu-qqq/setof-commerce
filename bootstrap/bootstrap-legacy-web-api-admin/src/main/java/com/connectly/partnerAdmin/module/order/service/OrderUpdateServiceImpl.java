package com.connectly.partnerAdmin.module.order.service;


import com.connectly.partnerAdmin.module.order.dto.UpdateOrderResponse;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.service.strategy.OrderUpdateStrategyProvider;
import com.connectly.partnerAdmin.module.order.service.strategy.OrderUpdateStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderUpdateServiceImpl<T extends UpdateOrder> implements OrderUpdateService<T> {

    private final OrderUpdateStrategyProvider<T> orderUpdateStrategy;

    @Override
    public UpdateOrderResponse updateOrder(T updateOrder) {
        OrderUpdateStrategy<T> service = orderUpdateStrategy.get(updateOrder.getOrderStatus());
        return service.updateOrder(updateOrder);
    }


    @Override
    public List<UpdateOrderResponse> updateOrders(List<T> updateOrders) {
        Map<OrderStatus, List<T>> ordersGroupedByStatus = toGroupByOrderStatus(updateOrders);
        List<UpdateOrderResponse> responses = new ArrayList<>();

        ordersGroupedByStatus.forEach((orderStatus, orders) -> {
            OrderUpdateStrategy<T> service = orderUpdateStrategy.get(orderStatus);
            List<UpdateOrderResponse> updateOrderResponses = service.updateOrders(orders);
            responses.addAll(updateOrderResponses);
        });
        return responses;
    }


    private Map<OrderStatus, List<T>> toGroupByOrderStatus(List<T> updateOrders) {
        return updateOrders.stream()
                .collect(Collectors
                        .groupingBy(UpdateOrder::getOrderStatus));
    }

    @Override
    public UpdateOrderResponse updateOrderExternalMall(T dto) {
        OrderUpdateStrategy<T> service = orderUpdateStrategy.get(dto.getOrderStatus());
        return service.updateOrder(dto);
    }


}
