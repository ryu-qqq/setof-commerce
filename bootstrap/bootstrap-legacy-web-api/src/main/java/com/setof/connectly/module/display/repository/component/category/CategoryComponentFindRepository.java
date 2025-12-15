package com.setof.connectly.module.display.repository.component.category;

import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;
import java.util.Set;

public interface CategoryComponentFindRepository {

    List<ProductGroupThumbnail> fetchCategoryComponentsWhenLesserThanExposedSize(
            long componentId, Set<Long> productIds, ComponentFilter filter, int pageSize);
}
