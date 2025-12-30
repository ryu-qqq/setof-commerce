package com.connectly.partnerAdmin.module.product.service.stock;

import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Transactional
@RequiredArgsConstructor
@Service
public class ProductStockUpdateServiceImpl implements ProductStockUpdateService{

    private final InventoryService inventoryService;

    @Override
    public Set<ProductFetchResponse> updateProductStock(long productId, UpdateProductStock stock) {
        return inventoryService.update(Collections.singletonList(stock));
    }

    @Override
    public Set<ProductFetchResponse> updateProductStocks(long productGroupId, List<UpdateProductStock> updateProductStocks) {
        return inventoryService.update(updateProductStocks);
    }

    @Override
    public Set<ProductFetchResponse> outOfStock(long productGroupId) {
        return inventoryService.soldOut(productGroupId);
    }

}
