package com.setof.connectly.module.product.service.stock;

import com.setof.connectly.module.product.dto.group.ProductGroupPatchResponse;
import com.setof.connectly.module.product.dto.stock.UpdateProductStock;
import java.util.List;

public interface ProductStockQueryService {
    ProductGroupPatchResponse updateProductStock(UpdateProductStock stock);

    List<ProductGroupPatchResponse> updateProductStocks(
            List<UpdateProductStock> updateProductStocks);
}
