package com.setof.connectly.module.notification.aop;

import com.setof.connectly.module.notification.service.slack.SlackOrderIssueService;
import com.setof.connectly.module.payment.entity.PaymentBill;
import com.setof.connectly.module.portone.dto.PgProviderTransDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(99)
@Aspect
@Component
@RequiredArgsConstructor
public class SlackOrderTriggerAop {
    private final SlackOrderIssueService slackOrderIssueService;

    @Pointcut(
            value =
                    "execution(* com.setof.connectly.module.payment.service.pay.*.doPayWebHook(..))"
                            + " && args(pgProviderTransDto)")
    private void triggerPayCompletedPointCut(PgProviderTransDto pgProviderTransDto) {}

    @AfterReturning(
            value = "triggerPayCompletedPointCut(pgProviderTransDto)",
            returning = "paymentBill",
            argNames = "pjp, paymentBill, pgProviderTransDto")
    public void triggerSlackMessageOrderIssue(
            JoinPoint pjp,
            Optional<PaymentBill> paymentBill,
            PgProviderTransDto pgProviderTransDto) {
        paymentBill.ifPresent(
                p -> {
                    slackOrderIssueService.sendSlackMessage(p.getPaymentId());
                });
    }
}
