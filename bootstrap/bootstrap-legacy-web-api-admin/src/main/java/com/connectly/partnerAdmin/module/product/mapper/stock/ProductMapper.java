package com.connectly.partnerAdmin.module.product.mapper.stock;

import com.connectly.partnerAdmin.module.product.dto.query.CreateOption;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;

import java.util.List;
import java.util.Set;

public interface ProductMapper {
    Set<Product> toProducts(List<CreateOption> options);

}
