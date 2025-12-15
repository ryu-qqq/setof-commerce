package com.setof.connectly.module.display.repository.component;

import com.setof.connectly.module.display.dto.component.NotProductRelatedComponents;
import java.util.List;

public interface NotRelatedProductComponentRepository {

    NotProductRelatedComponents fetchNonRelatedProductComponent(
            long contentId, List<Long> componentIds);
}
