package com.setof.connectly.module.discount.aop;

import com.setof.connectly.module.discount.service.DiscountApplyService;
import com.setof.connectly.module.order.entity.snapshot.group.OrderSnapShotProductGroup;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ApplyDiscountSnapShotAop {

    private final DiscountApplyService discountApplyService;

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.order.service.query.snapshot.group.ProductGroupSnapShotQueryService.saveSnapShot(..))"
                        + " && args(snapShots)")
    private void applyDiscountOrderSnapShotPointCut(Set<OrderSnapShotProductGroup> snapShots) {}

    @Around(value = "applyDiscountOrderSnapShotPointCut(snapShots)", argNames = "pjp, snapShots")
    public Object applyDiscountOrderSnapShot(
            ProceedingJoinPoint pjp, Set<OrderSnapShotProductGroup> snapShots) throws Throwable {
        applyDiscountsToOrderSnapShotProducts(snapShots);
        return pjp.proceed();
    }

    private void applyDiscountsToOrderSnapShotProducts(
            Set<OrderSnapShotProductGroup> orderSnapShotProductGroups) {
        discountApplyService.applyDiscountsOffer(orderSnapShotProductGroups);
    }
}
