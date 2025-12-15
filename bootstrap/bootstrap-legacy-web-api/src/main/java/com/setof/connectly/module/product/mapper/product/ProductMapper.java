package com.setof.connectly.module.product.mapper.product;

import com.setof.connectly.module.product.dto.group.ProductGroupPatchResponse;
import com.setof.connectly.module.product.entity.group.Product;

public interface ProductMapper {

    ProductGroupPatchResponse toProductPatchResponse(Product product);
}
