package com.connectly.partnerAdmin.module.discount.aop;

import com.connectly.partnerAdmin.module.discount.service.DiscountApplyService;
import com.connectly.partnerAdmin.module.display.dto.component.DisplayProductGroupThumbnail;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.content.ContentGroupResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class ApplyDiscountDisplayModuleAspect extends ApplyDiscountAspect {

    public ApplyDiscountDisplayModuleAspect(DiscountApplyService discountApplyService) {
        super(discountApplyService);
    }

    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.display.service.content.ContentFetchServiceImpl(..))")
    private void applyDiscountContentsPointCut() {}


    @AfterReturning(value = "applyDiscountContentsPointCut()", returning = "contentGroupResponse")
    public void applyDiscountFetchProuct(JoinPoint joinPoint, ContentGroupResponse contentGroupResponse) throws Throwable {

        List<DisplayProductGroupThumbnail> productGroupThumbnails = contentGroupResponse.getComponents()
                .stream()
                .filter(componentDetailResponse -> componentDetailResponse.getComponentType().isProductRelatedContents())
                .map(SubComponent::getProductGroupThumbnails)
                .flatMap(List::stream)
                .toList();

        applyDiscountsToProducts(productGroupThumbnails);
    }

}
