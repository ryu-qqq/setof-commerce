package com.setof.connectly.module.product.mapper;

import com.setof.connectly.module.product.dto.ProductGroupResponse;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.dto.cateogry.ProductCategoryDto;
import com.setof.connectly.module.product.dto.group.fetch.ProductGroupFetchDto;
import java.util.List;

public interface ProductGroupMapper {
    ProductGroupResponse toProductGroupResponse(
            ProductGroupFetchDto productGroup, List<ProductCategoryDto> productCategoryList);

    List<ProductGroupThumbnail> reOrderProductGroupThumbnail(
            List<Long> productGroupIds, List<ProductGroupThumbnail> productGroupThumbnails);
}
