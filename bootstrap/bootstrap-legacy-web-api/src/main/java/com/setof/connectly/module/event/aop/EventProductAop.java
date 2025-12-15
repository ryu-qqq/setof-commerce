package com.setof.connectly.module.event.aop;

import com.setof.connectly.module.event.enums.EventProductType;
import com.setof.connectly.module.event.service.product.EventProductFindService;
import com.setof.connectly.module.product.dto.ProductGroupResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class EventProductAop {

    private final EventProductFindService eventProductFindService;

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.product.service.group.fetch.ProductGroupFindService.fetchProductGroup(..))")
    private void isEventProduct() {}

    @AfterReturning(
            value = "isEventProduct()",
            returning = "productGroupResponse",
            argNames = "joinPoint,productGroupResponse")
    public void isEventProduct(JoinPoint joinPoint, ProductGroupResponse productGroupResponse)
            throws Throwable {
        long productGroupId = productGroupResponse.getProductGroup().getProductGroupId();
        EventProductType eventProductType =
                eventProductFindService.fetchEventProductType(productGroupId);
        productGroupResponse.setEventProductType(eventProductType);
    }
}
