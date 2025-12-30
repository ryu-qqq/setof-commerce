package com.connectly.partnerAdmin.module.notification.aop;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.external.entity.ExternalOrder;
import com.connectly.partnerAdmin.module.notification.service.slack.SlackOrderIssueService;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class SlackLfOrderTriggerAop {
    private final SlackOrderIssueService slackOrderIssueService;

    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.external.service.order.lf.LfOrderIssueService.syncOrder(..))")
    private void triggerPayCompletedPointCut(){}

    @AfterReturning(value = "triggerPayCompletedPointCut()", returning = "interlockingOrders", argNames = "pjp, interlockingOrders")
    public void triggerOrderAlimTalk(JoinPoint pjp, List<ExternalOrder> interlockingOrders) {

        Set<Long> paymentIds = interlockingOrders.stream()
                .map(ExternalOrder::getPaymentId)
                .collect(Collectors.toSet());

        paymentIds.forEach(slackOrderIssueService::sendSlackMessage);

    }

}
