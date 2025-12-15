package com.setof.connectly.module.notification.aop;

import com.setof.connectly.module.notification.dto.order.OrderStatusTransferDto;
import com.setof.connectly.module.notification.service.alimtalk.OrderAlimTalkService;
import com.setof.connectly.module.order.dto.fetch.UpdateOrderResponse;
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
public class AlimTalkOrderTriggerAop {

    private final OrderAlimTalkService orderAlimTalkService;

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.order.service.query.update.strategy.*.updateOrder(..))")
    private void triggerOrderAlimTalkPointCut() {}

    @AfterReturning(
            value = "triggerOrderAlimTalkPointCut()",
            returning = "updateOrderResponses",
            argNames = "pjp,updateOrderResponses")
    public void triggerOrderAlimTalk(
            JoinPoint pjp, List<UpdateOrderResponse> updateOrderResponses) {

        List<OrderStatusTransferDto> orderStatusTransfers =
                updateOrderResponses.stream()
                        .map(
                                o ->
                                        OrderStatusTransferDto.builder()
                                                .orderId(o.getOrderId())
                                                .orderStatus(o.getToBeOrderStatus())
                                                .reason(o.getChangeReason())
                                                .detailReason(o.getChangeDetailReason())
                                                .build())
                        .collect(Collectors.toList());

        orderAlimTalkService.sendAlimTalk(orderStatusTransfers);
    }
}
