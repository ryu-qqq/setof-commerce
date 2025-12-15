package com.setof.connectly.module.mileage.aop;

import com.setof.connectly.module.mileage.service.query.MileageQueryService;
import com.setof.connectly.module.order.dto.query.RefundOrderInfo;
import com.setof.connectly.module.payment.entity.PaymentBill;
import com.setof.connectly.module.portone.dto.PgProviderTransDto;
import com.setof.connectly.module.utils.SecurityUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Aspect
@Component
@RequiredArgsConstructor
public class MileageHandleAfterPayAop {
    private final MileageQueryService mileageQueryService;

    @Pointcut(
            value =
                    "execution(* com.setof.connectly.module.payment.service.pay.*.doPayWebHook(..))"
                            + " && args(pgProviderTransDto)")
    public void doPayWebhookPointcut(PgProviderTransDto pgProviderTransDto) {}

    @Pointcut(
            value =
                    "execution(* com.setof.connectly.module.payment.service.pay.*.doPayRefund(..))"
                            + " && args(pgProviderTransDto, refundOrderInfo)",
            argNames = "pgProviderTransDto, refundOrderInfo")
    public void doPayRefundPointcut(
            PgProviderTransDto pgProviderTransDto, RefundOrderInfo refundOrderInfo) {}

    @AfterReturning(
            value = "doPayWebhookPointcut(pgProviderTransDto)",
            returning = "paymentBillOpt",
            argNames = "pjp,pgProviderTransDto,paymentBillOpt")
    public void mileageDeductAfterPayComplete(
            JoinPoint pjp,
            PgProviderTransDto pgProviderTransDto,
            Optional<PaymentBill> paymentBillOpt)
            throws Throwable {
        if (pgProviderTransDto.getPaymentStatus().isCompleted()) {
            paymentBillOpt.ifPresent(this::deductMileage);
        }
    }

    @Around(
            value = "doPayRefundPointcut(pgProviderTransDto, refundOrderInfo)",
            argNames = "pjp,pgProviderTransDto, refundOrderInfo")
    public Object mileageRollBackAfterPayRefund(
            ProceedingJoinPoint pjp,
            PgProviderTransDto pgProviderTransDto,
            RefundOrderInfo refundOrderInfo)
            throws Throwable {
        Object proceed = pjp.proceed();
        if (refundOrderInfo.getOrderStatus().isCancelOrder()) {
            rollBackMileage(pgProviderTransDto.getPaymentId(), refundOrderInfo);
        }
        return proceed;
    }

    protected void deductMileage(PaymentBill bill) {
        mileageQueryService.deductMileage(
                bill.getPaymentId(),
                bill.getUserId(),
                bill.getPaymentAmount(),
                bill.getUsedMileageAmount());
    }

    protected void rollBackMileage(long paymentId, RefundOrderInfo refundOrderInfo) {
        long userId = SecurityUtils.currentUserId();
        if (refundOrderInfo.getExpectedRefundMileage() > 0)
            mileageQueryService.rollBackMileage(paymentId, userId, refundOrderInfo);
    }
}
