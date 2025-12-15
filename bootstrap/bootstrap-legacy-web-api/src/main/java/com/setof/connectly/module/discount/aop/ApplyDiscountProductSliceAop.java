package com.setof.connectly.module.discount.aop;

import com.setof.connectly.module.common.filter.ItemFilter;
import com.setof.connectly.module.discount.service.DiscountApplyService;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
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
public class ApplyDiscountProductSliceAop {

    private final DiscountApplyService discountApplyService;

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.product.mapper.ProductSliceMapperImpl.toSlice(..))"
                        + " && args(data, pageable, totalElements)",
            argNames = "data, pageable, totalElements")
    private void applyDiscountProductPointCut(
            List<ProductGroupThumbnail> data, Pageable pageable, long totalElements) {}

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.product.mapper.ProductSliceMapper.toSlice(..))"
                        + " && args(data, pageable, totalElements, filter)",
            argNames = "data, pageable, totalElements, filter")
    private void applyDiscountProductWithFilter(
            List<ProductGroupThumbnail> data,
            Pageable pageable,
            long totalElements,
            ItemFilter filter) {}

    @Around(
            value = "applyDiscountProductPointCut(data, pageable, totalElements)",
            argNames = "pjp, data, pageable, totalElements")
    public Object applyDiscountProducts(
            ProceedingJoinPoint pjp,
            List<ProductGroupThumbnail> data,
            Pageable pageable,
            long totalElements)
            throws Throwable {
        applyDiscountsToProducts(data);
        return pjp.proceed();
    }

    @Around(
            value = "applyDiscountProductWithFilter(data, pageable, totalElements, filter)",
            argNames = "pjp, data, pageable, totalElements, filter")
    public Object applyDiscountProducts(
            ProceedingJoinPoint pjp,
            List<ProductGroupThumbnail> data,
            Pageable pageable,
            long totalElements,
            ItemFilter filter)
            throws Throwable {
        applyDiscountsToProducts(data);
        return pjp.proceed();
    }

    private void applyDiscountsToProducts(List<ProductGroupThumbnail> productGroups) {
        discountApplyService.applyDiscountsOffer(productGroups);
    }
}
