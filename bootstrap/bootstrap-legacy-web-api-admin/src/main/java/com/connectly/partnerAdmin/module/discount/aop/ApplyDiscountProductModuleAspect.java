package com.connectly.partnerAdmin.module.discount.aop;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.discount.service.DiscountApplyService;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupFetchResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApplyDiscountProductModuleAspect extends ApplyDiscountAspect {

    public ApplyDiscountProductModuleAspect(DiscountApplyService discountApplyService) {
        super(discountApplyService);
    }


    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.product.service.group.ProductGroupFetchServiceImpl.fetchProductGroup(..))")
    private void applyDiscount() {}


    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.product.service.group.ProductGroupFetchServiceImpl.fetchProductGroups(..))")
    private void applyDiscounts() {}


    @AfterReturning(value = "applyDiscount()", returning = "productGroupFetchResponse", argNames = "jp, productGroupFetchResponse")
    public void applyDiscountToProductOrProducts(JoinPoint jp, ProductGroupFetchResponse productGroupFetchResponse) throws Throwable {
        applyDiscountToProduct(productGroupFetchResponse);
    }

    @AfterReturning(value = "applyDiscounts()", returning = "productGroupDetailResponses", argNames = "jp, productGroupDetailResponses")
    public void applyDiscountToProducts(JoinPoint jp, CustomPageable<ProductGroupDetailResponse> productGroupDetailResponses) throws Throwable {
        applyDiscountsToProducts(productGroupDetailResponses.getContent());

    }
}
