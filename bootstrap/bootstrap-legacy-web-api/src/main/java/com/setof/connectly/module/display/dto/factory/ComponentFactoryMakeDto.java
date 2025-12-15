package com.setof.connectly.module.display.dto.factory;

import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.Collections;
import java.util.List;

public interface ComponentFactoryMakeDto {

    SubComponent toComponentDetail();

    long getContentId();

    long getComponentId();

    long getSubComponentId();

    default Long getTargetId() {
        return 0L;
    }

    default List<Long> getTargetIds() {
        return Collections.emptyList();
    }

    default List<ProductGroupThumbnail> getProductGroupThumbnails() {
        return Collections.emptyList();
    }
}
