package com.connectly.partnerAdmin.module.product.service.stock;

import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductStock;

import java.util.List;
import java.util.Set;

public interface ProductStockUpdateService {

    Set<ProductFetchResponse> updateProductStock(long productId, UpdateProductStock stock);
    Set<ProductFetchResponse> updateProductStocks(long productGroupId, List<UpdateProductStock> updateProductStocks);
    Set<ProductFetchResponse> outOfStock(long productGroupId);

}
