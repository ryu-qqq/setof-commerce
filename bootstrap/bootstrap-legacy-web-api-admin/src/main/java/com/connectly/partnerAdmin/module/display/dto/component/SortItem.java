package com.connectly.partnerAdmin.module.display.dto.component;

import com.connectly.partnerAdmin.module.display.enums.SortType;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SortItem {
    private SortType sortType;
    private List<DisplayProductGroupThumbnail> productGroups;

}
