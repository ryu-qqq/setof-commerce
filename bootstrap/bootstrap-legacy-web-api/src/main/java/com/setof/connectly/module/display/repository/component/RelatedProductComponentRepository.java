package com.setof.connectly.module.display.repository.component;

import com.setof.connectly.module.display.dto.component.ProductRelatedComponents;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.display.dto.query.ComponentItemQueryDto;
import java.util.List;

public interface RelatedProductComponentRepository {
    ProductRelatedComponents fetchRelatedProductComponent(long contentId, List<Long> componentIds);

    List<ComponentItemQueryDto> fetchComponentItemQueries(List<Long> componentIds);

    List<ComponentItemQueryDto> fetchComponentItemQueries(
            List<Long> componentIds, ComponentFilter filter);
}
