package com.connectly.partnerAdmin.module.product.aop;


import com.connectly.partnerAdmin.module.category.service.CategoryFetchService;
import com.connectly.partnerAdmin.module.product.filter.ProductGroupFilter;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Aspect
@Component
@RequiredArgsConstructor
public class CategoryChildrenFetchAspect {

    private final CategoryFetchService categoryFetchService;

    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.product.service.group.ProductGroupFetchServiceImpl.fetchProductGroups(..)) && args(filter, pageable)", argNames = "filter,pageable")
    private void fetchProductGroups(ProductGroupFilter filter, Pageable pageable) {}


    @Before(value = "fetchProductGroups(filter, pageable)", argNames = "joinPoint, filter, pageable")
    public void fetchCategoryChildren(JoinPoint joinPoint , ProductGroupFilter filter, Pageable pageable) {
        if(filter.getCategoryId() != null){
            Set<Long> categoryIds = categoryFetchService.fetchCategoryChildrenIds(filter.getCategoryId());
            filter.setCategoryIds(categoryIds);
        }else{
            filter.setCategoryIds(Collections.emptySet());
        }
    }



}
