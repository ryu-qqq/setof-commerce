package com.connectly.partnerAdmin.module.notification.aop;


import com.connectly.partnerAdmin.module.notification.mapper.order.OrderStatusTransferDto;
import com.connectly.partnerAdmin.module.notification.service.alimtalk.OrderAlimTalkService;
import com.connectly.partnerAdmin.module.order.dto.UpdateOrderResponse;
import com.connectly.partnerAdmin.module.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Aspect
@Component
@RequiredArgsConstructor
public class AlimTalkOrderTriggerAop {

    private final OrderAlimTalkService orderAlimTalkService;

    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.order.service.OrderUpdateServiceImpl.updateOrder(..))")
    private void triggerOrderUpdateAlimTalkPointCut(){}

    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.order.service.OrderUpdateServiceImpl.updateOrders(..))")
    private void triggerOrdersUpdateAlimTalksPointCut(){}

    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.order.service.OrderIssueServiceImpl.doOrders(..))")
    private void triggerOrderIssueAlimTalkPointCut(){}

    @AfterReturning(value = "triggerOrderUpdateAlimTalkPointCut()", returning = "updateOrderResponse", argNames = "pjp,updateOrderResponse")
    public void triggerOrderAlimTalk(JoinPoint pjp, UpdateOrderResponse updateOrderResponse) {

        OrderStatusTransferDto orderStatusTransfer = OrderStatusTransferDto.builder()
                .orderId(updateOrderResponse.getOrderId())
                .orderStatus(updateOrderResponse.getToBeOrderStatus())
                .reason(updateOrderResponse.getChangeReason())
                .detailReason(updateOrderResponse.getChangeDetailReason())
                .build();

        sendAlimTalk(Collections.singletonList(orderStatusTransfer));
    }


    @AfterReturning(value = "triggerOrdersUpdateAlimTalksPointCut()", returning = "updateOrderResponses", argNames = "pjp,updateOrderResponses")
    public void triggerOrderAlimTalk(JoinPoint pjp, List<UpdateOrderResponse> updateOrderResponses) {

        List<OrderStatusTransferDto> orderStatusTransfers = updateOrderResponses.stream()
                .map(o -> OrderStatusTransferDto.builder()
                        .orderId(o.getOrderId())
                        .orderStatus(o.getToBeOrderStatus())
                        .reason(o.getChangeReason())
                        .detailReason(o.getChangeDetailReason())
                        .build())
                .collect(Collectors.toList());

        sendAlimTalk(orderStatusTransfers);
    }


    @AfterReturning(value = "triggerOrderIssueAlimTalkPointCut()", returning = "orders", argNames = "pjp,orders")
    public void triggerOrderIssueAlimTalk(JoinPoint pjp, List<Order> orders) {
        List<OrderStatusTransferDto> orderStatusTransfers = orders.stream()
                .map(order -> OrderStatusTransferDto.builder()
                        .orderId(order.getId())
                        .orderStatus(order.getOrderStatus())
                        .build()
                    ).collect(Collectors.toList());

        sendAlimTalk(orderStatusTransfers);
    }

    private void sendAlimTalk(List<OrderStatusTransferDto> orderStatusTransfers) {
        List<OrderStatusTransferDto> filterdList = orderStatusTransfers.stream()
                .filter(dto -> dto.getOrderStatus().alimTalkOrderStatus())
                .toList();
        orderAlimTalkService.sendAlimTalk(filterdList);
    }

}
