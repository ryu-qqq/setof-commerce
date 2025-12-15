package com.setof.connectly.module.event.aop;

import com.setof.connectly.module.event.dto.EventMileageDto;
import com.setof.connectly.module.event.enums.EventMileageType;
import com.setof.connectly.module.event.service.mileage.EventMileageFindService;
import com.setof.connectly.module.user.entity.UserMileage;
import com.setof.connectly.module.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class JoinMileageAop {

    private final EventMileageFindService eventMileageFindService;

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.user.service.query.UserQueryService.saveUser(..))"
                        + " && args(users)")
    private void setMileage(Users users) {}

    @Before(value = "setMileage(users)", argNames = "joinPoint, users")
    public void setUserMileage(JoinPoint joinPoint, Users users) throws Throwable {
        EventMileageDto eventMileage =
                eventMileageFindService.fetchEventMileage(EventMileageType.JOIN);
        UserMileage userMileage = new UserMileage(eventMileage.getMileageAmount());
        users.setUserMileage(userMileage);
    }
}
