package com.connectly.partnerAdmin.module.product.mapper.group;

import com.connectly.partnerAdmin.module.category.core.TreeCategoryContext;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;

import java.util.List;

public interface ProductGroupCategoryMapper {

    void setCategoryFullPath(List<ProductGroupDetailResponse> productGroupDetailResponses, List<TreeCategoryContext> treeCategoryContexts);

}
