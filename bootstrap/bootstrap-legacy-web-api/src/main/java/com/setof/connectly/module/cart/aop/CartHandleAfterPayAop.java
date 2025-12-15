package com.setof.connectly.module.cart.aop;

import com.setof.connectly.module.cart.dto.CartDeleteRequestDto;
import com.setof.connectly.module.cart.service.query.CartQueryService;
import com.setof.connectly.module.payment.dto.payment.CreatePaymentInCart;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Aspect
@Component
@RequiredArgsConstructor
public class CartHandleAfterPayAop {

    private final CartQueryService cartQueryService;

    @Pointcut(
            value =
                    "execution(* com.setof.connectly.module.payment.service.pay.*.doPayInCart(..))"
                            + " && args(createPaymentInCart)")
    public void payInCartPointcut(CreatePaymentInCart createPaymentInCart) {}

    @Pointcut(
            value =
                    "execution(* com.setof.connectly.module.payment.service.pay.*.doPayFailed(..))"
                            + " && args(paymentId, cartIds)",
            argNames = "paymentId,cartIds")
    public void afterPayFailed(long paymentId, List<Long> cartIds) {}

    @Around(value = "payInCartPointcut(createPaymentInCart)", argNames = "pjp,createPaymentInCart")
    public Object cartDeleteAfterPayComplete(
            ProceedingJoinPoint pjp, CreatePaymentInCart createPaymentInCart) throws Throwable {
        Object proceed = pjp.proceed();
        deleteCart(createPaymentInCart);
        return proceed;
    }

    @Around(value = "afterPayFailed(paymentId, cartIds)", argNames = "pjp,paymentId, cartIds")
    public Object cartRollbackAfterPayFailed(
            ProceedingJoinPoint pjp, long paymentId, List<Long> cartIds) throws Throwable {
        Object proceed = pjp.proceed();
        rollBackCart(cartIds);
        return proceed;
    }

    private void deleteCart(CreatePaymentInCart payment) {
        List<Long> cartIds = payment.getCartIds();
        cartIds.forEach(
                aLong -> {
                    CartDeleteRequestDto cartDeleteRequestDto = new CartDeleteRequestDto(aLong);
                    cartQueryService.delete(cartDeleteRequestDto);
                });
    }

    protected void rollBackCart(List<Long> cartIds) {
        if (cartIds != null) {
            if (!cartIds.isEmpty()) cartQueryService.rollBack(cartIds);
        }
    }
}
