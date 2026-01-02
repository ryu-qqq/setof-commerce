package com.connectly.partnerAdmin.module.display.repository.component.item;

import com.connectly.partnerAdmin.module.display.dto.component.DisplayProductGroupThumbnail;
import com.connectly.partnerAdmin.module.display.entity.component.ComponentItem;
import com.connectly.partnerAdmin.module.display.enums.SortType;

import java.util.List;

public interface ComponentItemJdbcRepository {

    void saveAll(List<ComponentItem> componentItems);
    void deleteAll(List<Long> componentIds);
    void deleteAllWithTabIds(List<Long> tabIds);
    void deleteAll(long componentId, SortType sortType, List<Long> productGroupIds);
    void updateAll(long componentId, long componentTargetId, List<DisplayProductGroupThumbnail> updatedProducts);
}
