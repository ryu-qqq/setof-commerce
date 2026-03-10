package com.ryuqq.setof.application.product.port.out.query;

import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.List;
import java.util.Optional;

public interface ProductQueryPort {
    Optional<Product> findById(ProductId id);

    List<Product> findByProductGroupId(ProductGroupId productGroupId);

    List<Product> findByIds(List<ProductId> ids);
}
