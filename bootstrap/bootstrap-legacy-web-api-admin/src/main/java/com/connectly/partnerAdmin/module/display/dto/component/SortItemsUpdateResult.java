package com.connectly.partnerAdmin.module.display.dto.component;

import com.connectly.partnerAdmin.module.display.enums.SortType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SortItemsUpdateResult {

    private Map<SortType, List<DisplayProductGroupThumbnail>> addedItems;
    private Map<SortType, List<DisplayProductGroupThumbnail>> updatedItems;
    private Map<SortType, List<DisplayProductGroupThumbnail>> deletedItems;



}
