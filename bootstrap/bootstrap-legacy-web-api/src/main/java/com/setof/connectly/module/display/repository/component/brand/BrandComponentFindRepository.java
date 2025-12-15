package com.setof.connectly.module.display.repository.component.brand;

import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;
import java.util.Set;

public interface BrandComponentFindRepository {

    List<ProductGroupThumbnail> fetchBrandComponentsWhenLesserThanExposedSize(
            long componentId, Set<Long> productIds, ComponentFilter filterDto, int pageSize);
}
