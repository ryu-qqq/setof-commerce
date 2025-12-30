package com.connectly.partnerAdmin.module.product.mapper.group;

import com.connectly.partnerAdmin.module.product.dto.query.CreateProductGroup;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductGroup;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;

public interface ProductGroupMapper {

    ProductGroup toProductGroup(String externalProductId, CreateProductGroup createProductGroup);
    UpdateProductGroup toUpdateProductGroup(CreateProductGroup createProductGroup);
}
