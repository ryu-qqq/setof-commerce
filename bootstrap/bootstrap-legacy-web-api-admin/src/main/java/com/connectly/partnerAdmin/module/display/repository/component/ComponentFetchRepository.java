package com.connectly.partnerAdmin.module.display.repository.component;

import com.connectly.partnerAdmin.module.display.dto.component.DisplayProductGroupThumbnail;

import java.util.List;

public interface ComponentFetchRepository {

    List<DisplayProductGroupThumbnail> getComponentRegisteredProducts(long componentId);
}
