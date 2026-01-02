package com.connectly.partnerAdmin.module.display.repository.component.image;

import com.connectly.partnerAdmin.module.display.dto.component.image.ImageComponentLink;
import com.connectly.partnerAdmin.module.display.entity.component.item.ImageComponentItem;
import com.connectly.partnerAdmin.module.display.entity.component.sub.ImageComponent;

import java.util.List;

public interface ImageComponentItemJdbcRepository {

    void saveAll(List<ImageComponentItem> imageComponentItems);

    void deleteAll(List<Long> imageComponentIds);

    void updateAll(List<ImageComponentItem> imageComponentItems);
}
