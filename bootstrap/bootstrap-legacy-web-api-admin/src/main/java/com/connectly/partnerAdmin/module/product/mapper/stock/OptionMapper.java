package com.connectly.partnerAdmin.module.product.mapper.stock;

import com.connectly.partnerAdmin.module.product.dto.ProductFetchDto;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.query.CreateOption;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;

import java.util.List;
import java.util.Set;

public interface OptionMapper {

    Set<ProductFetchResponse> toProductFetchResponse(List<ProductFetchDto> products);
    Set<ProductFetchResponse> toProductFetchResponse(long productGroupId, Set<Product> updatedProducts);

    void setProductIdInCreateOptions(List<CreateOption> createOptions, List<Product> products);

}
