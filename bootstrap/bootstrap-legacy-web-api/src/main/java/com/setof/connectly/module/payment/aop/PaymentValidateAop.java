package com.setof.connectly.module.payment.aop;

import com.setof.connectly.module.exception.payment.PaymentLockException;
import com.setof.connectly.module.payment.dto.payment.BasePayment;
import com.setof.connectly.module.payment.dto.payment.CreatePayment;
import com.setof.connectly.module.payment.dto.payment.CreatePaymentInCart;
import com.setof.connectly.module.payment.service.pay.PaymentLockService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class PaymentValidateAop {
    private final PaymentLockService paymentLockService;

    @Before(
            "execution(*"
                + " com.setof.connectly.module.payment.service.pay.AbstractPayQueryService.doPay(..))"
                + " || execution(*"
                + " com.setof.connectly.module.payment.service.pay.AbstractPayQueryService.doPayInCart(..))")
    public void paymentValidation(JoinPoint joinPoint) throws Throwable {
        Object arg = joinPoint.getArgs()[0];

        if (arg instanceof CreatePayment) {
            CreatePayment createPayment = (CreatePayment) arg;
            processPaymentValidation(createPayment);
        }

        if (arg instanceof CreatePaymentInCart) {
            CreatePaymentInCart createPaymentInCart = (CreatePaymentInCart) arg;
            processPaymentValidation(createPaymentInCart);
        }
    }

    private void processPaymentValidation(BasePayment basePayment) {

        if (!paymentLockService.tryLock(basePayment.getProductIds())) {
            throw new PaymentLockException();
        }
    }
}
