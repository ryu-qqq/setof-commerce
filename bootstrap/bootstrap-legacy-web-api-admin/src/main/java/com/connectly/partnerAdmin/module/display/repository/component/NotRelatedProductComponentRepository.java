package com.connectly.partnerAdmin.module.display.repository.component;

import com.connectly.partnerAdmin.module.display.dto.component.NotProductRelatedComponents;

import java.util.List;

public interface NotRelatedProductComponentRepository{

    NotProductRelatedComponents fetchNonRelatedProductComponent(long contentId, List<Long> componentIds);
}
