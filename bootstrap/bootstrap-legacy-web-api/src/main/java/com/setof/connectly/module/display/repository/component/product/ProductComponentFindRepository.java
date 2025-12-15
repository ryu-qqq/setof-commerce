package com.setof.connectly.module.display.repository.component.product;

import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;
import java.util.Set;

public interface ProductComponentFindRepository {
    List<ProductGroupThumbnail> fetchProductComponentsWhenLesserThanExposedSize(
            long componentId, Set<Long> productIds, ComponentFilter filter, int pageSize);
}
