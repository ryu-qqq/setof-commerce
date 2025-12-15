package com.setof.connectly.module.notification.aop;

import com.setof.connectly.module.notification.service.alimtalk.UserAlimTalkService;
import com.setof.connectly.module.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Aspect
@Component
@RequiredArgsConstructor
public class AlimTalkUserTriggerAop {

    private final UserAlimTalkService userAlimTalkService;

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.user.service.query.UserQueryService.saveUser(..))")
    private void triggerUserJoinPointCut() {}

    @AfterReturning(
            value = "triggerUserJoinPointCut()",
            returning = "users",
            argNames = "pjp,users")
    public void afterMemberJoin(JoinPoint pjp, Users users) {
        if (StringUtils.hasText(users.getPhoneNumber())) {
            userAlimTalkService.sendAlimTalk(users);
        }
    }
}
