package com.setof.connectly.module.discount.aop;

import com.setof.connectly.module.discount.service.DiscountApplyService;
import com.setof.connectly.module.display.dto.component.ComponentDetailResponse;
import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.content.ContentGroupResponse;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ApplyDiscountContentsAop {

    private final DiscountApplyService discountApplyService;

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.display.service.content.ContentFindServiceImpl.fetchContent(..))")
    private void applyDiscountContentsPointCut() {}

    @Around(value = "applyDiscountContentsPointCut()")
    public Object applyDiscountContents(ProceedingJoinPoint pjp) throws Throwable {
        Object proceed = pjp.proceed();

        if (proceed instanceof ContentGroupResponse) {
            ContentGroupResponse contentGroupResponse = (ContentGroupResponse) proceed;
            List<ProductGroupThumbnail> productGroupThumbnails =
                    contentGroupResponse.getComponentDetails().stream()
                            .filter(
                                    componentDetailResponse ->
                                            componentDetailResponse
                                                    .getComponentType()
                                                    .isProductRelatedContents())
                            .map(ComponentDetailResponse::getComponent)
                            .map(SubComponent::getProductGroupThumbnails)
                            .flatMap(List::stream)
                            .collect(Collectors.toList());

            applyDiscountsToProducts(productGroupThumbnails);
        }
        return proceed;
    }

    private void applyDiscountsToProducts(List<ProductGroupThumbnail> productGroups) {
        discountApplyService.applyDiscountsOffer(productGroups);
    }
}
