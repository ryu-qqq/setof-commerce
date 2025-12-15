package com.setof.connectly.module.discount.aop;

import com.setof.connectly.module.discount.service.DiscountApplyService;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.dto.cateogry.ProductCategoryDto;
import com.setof.connectly.module.product.dto.group.fetch.ProductGroupFetchDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ApplyDiscountProductAop {

    private final DiscountApplyService discountApplyService;

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.product.service.group.fetch.ProductGroupFindServiceImpl.fetchProductGroupWithBrand(..))"
                        + " ||execution(*"
                        + " com.setof.connectly.module.product.service.group.fetch.ProductGroupFindServiceImpl.fetchProductGroupWithSeller(..))"
                        + " ||execution(*"
                        + " com.setof.connectly.module.product.mapper.ProductGroupMapper.reOrderProductGroupThumbnail(..))")
    private void applyDiscountProductListPointCut() {}

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.product.mapper.ProductGroupMapper.toProductGroupResponse(..))"
                        + " && args(productGroup, productCategoryList) ")
    private void applyDiscountProductPointCut(
            ProductGroupFetchDto productGroup, List<ProductCategoryDto> productCategoryList) {}

    @AfterReturning(
            value = "applyDiscountProductListPointCut()",
            returning = "data",
            argNames = "joinPoint,data")
    public void applyDiscountProductsThumbnail(
            JoinPoint joinPoint, List<ProductGroupThumbnail> data) {
        applyDiscountsToProducts(data);
    }

    @Before(
            value = "applyDiscountProductPointCut(productGroup, productCategoryList)",
            argNames = "joinPoint,productGroup, productCategoryList")
    public void applyDiscountProduct(
            JoinPoint joinPoint,
            ProductGroupFetchDto productGroup,
            List<ProductCategoryDto> productCategoryList) {
        applyDiscountToProduct(productGroup);
    }

    private void applyDiscountsToProducts(List<ProductGroupThumbnail> productGroups) {
        discountApplyService.applyDiscountsOffer(productGroups);
    }

    private void applyDiscountToProduct(ProductGroupFetchDto data) {
        discountApplyService.applyDiscountOffer(data);
    }
}
