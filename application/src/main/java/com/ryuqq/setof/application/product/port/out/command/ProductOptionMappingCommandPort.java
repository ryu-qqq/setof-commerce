package com.ryuqq.setof.application.product.port.out.command;

import com.ryuqq.setof.domain.product.aggregate.ProductOptionMapping;
import java.util.List;

public interface ProductOptionMappingCommandPort {
    void persistAll(List<ProductOptionMapping> mappings);

    void persistAllForProduct(Long productId, List<ProductOptionMapping> mappings);
}
