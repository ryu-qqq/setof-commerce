package com.connectly.partnerAdmin.module.product.service.stock;

import com.connectly.partnerAdmin.module.product.core.Sku;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;

import java.util.List;
import java.util.Set;

public interface InventoryService {

    Set<ProductFetchResponse> decrease(List<? extends Sku> skus);
    Set<ProductFetchResponse> increase(List<? extends Sku> skus);
    Set<ProductFetchResponse> update(List<? extends Sku> skus);
    Set<ProductFetchResponse> soldOut(long productGroupId);
}
