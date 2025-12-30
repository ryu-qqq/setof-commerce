package com.connectly.partnerAdmin.module.display.service.component.query.item;

import com.connectly.partnerAdmin.module.display.dto.component.DisplayProductGroupThumbnail;
import com.connectly.partnerAdmin.module.display.entity.component.ComponentItem;
import com.connectly.partnerAdmin.module.display.enums.SortType;

import java.util.List;

public interface ComponentItemQueryService {

    void saveComponentItems(List<ComponentItem> componentItem);

    void deleteComponentItems(List<Long> componentIds);
    void deleteComponentItemsWithTabIds(List<Long> tabIds);
    void deleteComponentItems(long componentId, SortType sortType, List<Long> productGroupIds);
    void updateComponentItems(long componentId, long componentTargetId, List<DisplayProductGroupThumbnail> updatedProducts);
}
