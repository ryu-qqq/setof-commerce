package com.connectly.partnerAdmin.module.category.mapper;

import com.connectly.partnerAdmin.module.category.core.ProductCategoryContext;
import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryPageableMapper {

    CustomPageable<ProductCategoryContext> toProductCategoryContext(List<ProductCategoryContext> productCategoryContexts, Pageable pageable, long total);

}
