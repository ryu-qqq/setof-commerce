package com.setof.connectly.module.product.aop;

import com.setof.connectly.module.common.filter.ItemFilter;
import com.setof.connectly.module.product.service.category.ProductCategoryFetchService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ProductCategoryFetchAop {

    private final ProductCategoryFetchService productCategoryFetchService;

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.display.service.content.ContentFindServiceImpl.fetchComponentProductGroups(..))"
                        + " ||execution(*"
                        + " com.setof.connectly.module.search.service.SearchFindServiceImpl.fetchSearchResults(..))"
                        + " ||execution(*"
                        + " com.setof.connectly.module.display.repository.component.brand.BrandComponentFindRepositoryImpl.fetchBrandComponentsWhenLesserThanExposedSize(..))"
                        + " ||execution(*"
                        + " com.setof.connectly.module.display.repository.component.category.CategoryComponentFindRepositoryImpl.fetchCategoryComponentsWhenLesserThanExposedSize(..))"
                        + " ||execution(*"
                        + " com.setof.connectly.module.product.service.group.fetch.ProductGroupFindServiceImpl.fetchProductGroups(..))")
    private void categoryIdAop() {}

    @Before(value = "categoryIdAop()", argNames = "jp")
    public void fetchCategoryChildren(JoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();
        for (Object arg : args) {
            if (arg instanceof ItemFilter) {
                ItemFilter filter = (ItemFilter) arg;
                if (filter.getCategoryId() != null) setCategoryIds(filter);
                else filter.setCategoryIds(new ArrayList<>());
                break;
            }
        }
    }

    private void setCategoryIds(ItemFilter filter) {
        List<Long> categoryIds =
                productCategoryFetchService.fetchCategoryChildren(filter.getCategoryId());
        filter.setCategoryIds(categoryIds);
    }
}
