package com.connectly.partnerAdmin.module.category.mapper;

import com.connectly.partnerAdmin.module.category.core.ProductCategoryContext;
import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryPageableMapperImpl implements CategoryPageableMapper{

    @Override
    public CustomPageable<ProductCategoryContext> toProductCategoryContext(List<ProductCategoryContext> productCategoryContexts, Pageable pageable, long total) {
        Long lastDomainId = productCategoryContexts.isEmpty() ? null : productCategoryContexts.getLast().getCategoryId();
        return new CustomPageable<>(productCategoryContexts, pageable, total, lastDomainId);
    }
}
