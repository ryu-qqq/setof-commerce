package com.connectly.partnerAdmin.module.notification.aop;


import com.connectly.partnerAdmin.module.external.entity.ExternalOrder;
import com.connectly.partnerAdmin.module.notification.service.slack.SlackOrderIssueService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
public class SlackOrderTriggerAop {
    private final SlackOrderIssueService slackOrderIssueService;

    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.external.service.order.ExternalOrderIssueServiceImpl.syncOrders(..))")
    private void triggerPayCompletedPointCut(){}

    @AfterReturning(value = "triggerPayCompletedPointCut()", returning = "interlockingOrders", argNames = "pjp, interlockingOrders")
    public void triggerOrderAlimTalk(JoinPoint pjp, List<ExternalOrder> interlockingOrders) {

        Set<Long> paymentIds = interlockingOrders.stream()
                .map(ExternalOrder::getPaymentId)
                .collect(Collectors.toSet());

        paymentIds.forEach(slackOrderIssueService::sendSlackMessage);

    }

}
