package com.connectly.partnerAdmin.module.product.dto.query;

import java.util.Set;

import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;

public record CreateProductGroupResponse(
        long productGroupId,
        long sellerId,
        Set<ProductFetchResponse> products
) {}
