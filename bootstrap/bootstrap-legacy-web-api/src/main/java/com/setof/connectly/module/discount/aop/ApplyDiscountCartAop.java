package com.setof.connectly.module.discount.aop;

import com.setof.connectly.module.cart.dto.CartResponse;
import com.setof.connectly.module.discount.service.DiscountApplyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ApplyDiscountCartAop {

    private final DiscountApplyService discountApplyService;

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.cart.mapper.CartSliceMapper.toSlice(..)) &&"
                        + " args(carts, pageable, totalCount)",
            argNames = "carts,pageable,totalCount")
    private void applyDiscountCartPointCut(
            List<CartResponse> carts, Pageable pageable, long totalCount) {}

    @Around(
            value = "applyDiscountCartPointCut(carts, pageable, totalCount)",
            argNames = "pjp,carts,pageable,totalCount")
    public Object applyDiscountCart(
            ProceedingJoinPoint pjp, List<CartResponse> carts, Pageable pageable, long totalCount)
            throws Throwable {
        applyDiscountsToProductsInCarts(carts);
        return pjp.proceed();
    }

    private void applyDiscountsToProductsInCarts(List<CartResponse> carts) {
        discountApplyService.applyDiscountsOffer(carts);
    }
}
