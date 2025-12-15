package com.setof.connectly.module.order.aop;

import com.setof.connectly.module.order.dto.fetch.UpdateOrderResponse;
import com.setof.connectly.module.order.entity.order.OrderHistory;
import com.setof.connectly.module.order.service.query.history.OrderHistoryQueryService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class OrderStatusHistoryRecordAop {

    private final OrderHistoryQueryService orderHistoryQueryService;

    @Pointcut(
            "execution(*"
                + " com.setof.connectly.module.order.service.query.update.strategy.*.updateOrder(..))")
    private void recordHistory() {}

    @AfterReturning(value = "recordHistory()", returning = "orderResponses")
    public void saveRecordOrderHistory(JoinPoint jp, List<UpdateOrderResponse> orderResponses)
            throws Throwable {
        List<OrderHistory> updateOrders =
                orderResponses.stream()
                        .map(
                                u ->
                                        new OrderHistory(
                                                u.getOrderId(),
                                                u.getChangeReason(),
                                                u.getChangeDetailReason(),
                                                u.getToBeOrderStatus()))
                        .collect(Collectors.toList());

        saveOrderHistories(updateOrders);
    }

    private void saveOrderHistories(List<OrderHistory> updateOrders) {
        orderHistoryQueryService.saveOrderHistoryEntities(updateOrders);
    }
}
