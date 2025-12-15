package com.setof.connectly.module.event.aop;

import com.setof.connectly.module.mileage.service.query.MileageQueryService;
import com.setof.connectly.module.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class JoinEventTriggerAop {

    private final MileageQueryService mileageQueryService;

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.user.service.query.UserQueryService.saveUser(..))")
    private void triggerMemberJoin() {}

    @AfterReturning(value = "triggerMemberJoin()", returning = "proceed", argNames = "pjp,proceed")
    public void afterMemberJoin(JoinPoint pjp, Object proceed) {
        if (proceed instanceof Users) {
            Users users = (Users) proceed;
            // mileageQueryService.saveMileageForJoining(users);
        }
    }
}
