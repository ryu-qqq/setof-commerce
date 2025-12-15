package com.setof.connectly.module.portone.aop;

import com.setof.connectly.module.portone.service.account.AccountValidationService;
import com.setof.connectly.module.user.dto.account.CreateRefundAccount;
import com.setof.connectly.module.user.dto.account.RefundAccountInfo;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AccountValidateAop {

    private final AccountValidationService accountValidationService;

    @Around(
            "execution(*"
                + " com.setof.connectly.module.user.service.account.RefundAccountQueryServiceImpl.saveRefundAccount(..))"
                + " || execution(*"
                + " com.setof.connectly.module.user.service.account.RefundAccountQueryServiceImpl.updateRefundAccount(..))")
    public Object paymentValidation(ProceedingJoinPoint joinPoint) throws Throwable {
        Object arg = joinPoint.getArgs()[0];
        if (arg instanceof CreateRefundAccount) {
            CreateRefundAccount createRefundAccount = (CreateRefundAccount) arg;
            boolean b = accountValidationService.validateAccount(createRefundAccount);
            if (!b) {
                return new RefundAccountInfo(b);
            }

            return joinPoint.proceed();
        }
        throw new IllegalArgumentException("Invalid argument type.");
    }
}
