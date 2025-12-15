package com.setof.connectly.module.product.service.individual.fetch;

import com.setof.connectly.module.product.entity.group.Product;
import java.util.List;
import java.util.Optional;

public interface ProductFindService {

    Optional<String> fetchOptionName(long productId);

    Product fetchProductEntity(long productId);

    List<Product> fetchProductEntities(List<Long> productIds);
}
