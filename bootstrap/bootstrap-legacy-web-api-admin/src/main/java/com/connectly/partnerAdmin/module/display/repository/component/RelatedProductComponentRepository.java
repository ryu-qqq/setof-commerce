package com.connectly.partnerAdmin.module.display.repository.component;

import com.connectly.partnerAdmin.module.display.dto.component.ProductRelatedComponents;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentItemQueryDto;

import java.util.List;

public interface RelatedProductComponentRepository {
    ProductRelatedComponents fetchRelatedProductComponent(long contentId, List<Long> componentIds);
    List<ComponentItemQueryDto> fetchComponentItemQueries(List<Long> componentIds);
}
