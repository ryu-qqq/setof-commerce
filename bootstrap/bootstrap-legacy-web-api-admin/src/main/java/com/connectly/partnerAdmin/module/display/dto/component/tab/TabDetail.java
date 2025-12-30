package com.connectly.partnerAdmin.module.display.dto.component.tab;


import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.enums.TabMovingType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TabDetail {

    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long tabId;
    private String tabName;
    private Yn stickyYn;
    private TabMovingType tabMovingType;
    private int displayOrder;
    private List<SortItem> sortItems;

    public TabDetail(Long tabId, String tabName, Yn stickyYn, TabMovingType tabMovingType, int displayOrder, List<SortItem> sortItems) {
        this.tabId = tabId;
        this.tabName = tabName;
        this.stickyYn = stickyYn;
        this.tabMovingType = tabMovingType;
        this.displayOrder = displayOrder;
        this.sortItems = sortItems;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabDetail that)) return false;
        if (!super.equals(o)) return false; // 상위 클래스의 equals 호출

        if (!Objects.equals(tabName, that.tabName)) return false;
        if (!Objects.equals(stickyYn, that.stickyYn)) return false;
        if (!Objects.equals(tabMovingType, that.tabMovingType)) return false;
        return displayOrder == that.displayOrder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tabName, stickyYn, tabMovingType, displayOrder);
    }
}
