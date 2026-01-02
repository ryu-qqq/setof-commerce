package com.connectly.partnerAdmin.module.discount.aop;

import com.connectly.partnerAdmin.module.discount.core.PriceHolder;
import com.connectly.partnerAdmin.module.discount.service.DiscountApplyService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class ApplyDiscountAspect {

    private final DiscountApplyService discountApplyService;

    protected void applyDiscountToProduct(PriceHolder productGroup) {
        discountApplyService.applyDiscount(productGroup);
    }

    protected void applyDiscountsToProducts(List<? extends PriceHolder> priceHolders) {
        discountApplyService.applyDiscount(priceHolders);
    }

}
