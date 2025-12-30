package com.connectly.partnerAdmin.module.product.repository.stock;

import java.util.List;
import java.util.Optional;

import com.connectly.partnerAdmin.module.product.dto.ProductFetchDto;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;

public interface ProductFetchRepository {

    Optional<Product> fetchProductEntity(long productId);
    List<ProductFetchDto> fetchProducts(List<Long> productGroupIds);
    List<ProductFetchDto> fetchProducts(Long productGroupId);

    List<Product> fetchProductEntities(Long productGroupId, List<Long> productIds);
}
