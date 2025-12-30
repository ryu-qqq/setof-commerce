package com.connectly.partnerAdmin.module.product.service.stock;

import java.util.List;
import java.util.Set;

import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;

public interface ProductFetchService {

    Product fetchProductEntity(long productId);
    List<Product> fetchProductEntities(Long productGroupId, List<Long> productIds);
    Set<ProductFetchResponse> fetchProducts(List<Long> productGroupIds);
    Set<ProductFetchResponse> fetchProducts(Long productGroupId);
}

