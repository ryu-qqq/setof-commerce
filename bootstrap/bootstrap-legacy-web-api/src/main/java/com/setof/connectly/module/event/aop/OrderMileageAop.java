package com.setof.connectly.module.event.aop;

import com.setof.connectly.module.cart.dto.CartResponse;
import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.event.dto.EventMileageDto;
import com.setof.connectly.module.event.enums.EventMileageType;
import com.setof.connectly.module.event.service.mileage.EventMileageFindService;
import com.setof.connectly.module.product.dto.ProductGroupResponse;
import com.setof.connectly.module.user.enums.UserGradeEnum;
import com.setof.connectly.module.utils.NumberUtils;
import com.setof.connectly.module.utils.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class OrderMileageAop {

    private final EventMileageFindService eventMileageFindService;

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.product.service.group.fetch.ProductGroupFindService.fetchProductGroup(..))")
    private void setMileage() {}

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.cart.service.fetch.CartFindService.fetchCartList(..))")
    private void setCartMileage() {}

    @AfterReturning(
            value = "setMileage()",
            returning = "productGroupResponse",
            argNames = "joinPoint,productGroupResponse")
    public void isEventProduct(JoinPoint joinPoint, ProductGroupResponse productGroupResponse)
            throws Throwable {
        EventMileageDto eventMileage =
                eventMileageFindService.fetchEventMileage(EventMileageType.ORDER);
        long salePrice = productGroupResponse.getProductGroup().getPrice().getSalePrice();
        productGroupResponse.setExpectedMileageAmount(getMileageAmount(eventMileage, salePrice));
        productGroupResponse.setMileageRate(eventMileage.getMileageRate());
    }

    @AfterReturning(
            value = "setCartMileage()",
            returning = "cartResponseCustomSlice",
            argNames = "joinPoint,cartResponseCustomSlice")
    public void isEventCart(JoinPoint joinPoint, CustomSlice<CartResponse> cartResponseCustomSlice)
            throws Throwable {
        EventMileageDto eventMileage =
                eventMileageFindService.fetchEventMileage(EventMileageType.ORDER);
        if (!cartResponseCustomSlice.isEmpty()) {
            List<CartResponse> cartResponses = cartResponseCustomSlice.getContent();
            cartResponses.forEach(
                    c -> {
                        long salePrice = c.getPrice().getSalePrice();
                        c.setExpectedMileageAmount(getMileageAmount(eventMileage, salePrice));
                        c.setMileageRate(eventMileage.getMileageRate());
                    });
        }
    }

    private long getMileageAmount(EventMileageDto eventMileage, long salePrice) {
        UserGradeEnum userGrade = SecurityUtils.getUserGrade();
        double mileageRate = eventMileage.getMileageRate() * userGrade.getMileageReserveRate();
        return NumberUtils.downDotNumber(mileageRate, salePrice);
    }
}
