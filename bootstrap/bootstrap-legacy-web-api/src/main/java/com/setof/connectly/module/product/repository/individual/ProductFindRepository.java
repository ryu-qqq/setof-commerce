package com.setof.connectly.module.product.repository.individual;

import com.setof.connectly.module.product.dto.option.OptionDto;
import com.setof.connectly.module.product.entity.group.Product;
import java.util.List;
import java.util.Optional;

public interface ProductFindRepository {
    List<OptionDto> fetchOptions(long productId);

    Optional<Product> fetchProductEntity(long productId);

    List<Product> fetchProductEntities(List<Long> productIds);
}
