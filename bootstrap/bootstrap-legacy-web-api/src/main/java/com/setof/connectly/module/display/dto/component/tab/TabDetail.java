package com.setof.connectly.module.display.dto.component.tab;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import com.setof.connectly.module.display.enums.tab.TabMovingType;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;
import java.util.Objects;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TabDetail {
    private long tabId;
    private String tabName;
    private Yn stickyYn;
    private TabMovingType tabMovingType;
    private DisplayPeriod displayPeriod;
    private int displayOrder;
    private List<ProductGroupThumbnail> productGroupThumbnails;

    public void setTabId(Long tabId) {
        this.tabId = tabId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabDetail)) return false;
        if (!super.equals(o)) return false; // 상위 클래스의 equals 호출

        TabDetail that = (TabDetail) o;

        if (!Objects.equals(tabName, that.tabName)) return false;
        if (!Objects.equals(stickyYn, that.stickyYn)) return false;
        if (!Objects.equals(tabMovingType, that.tabMovingType)) return false;
        if (!Objects.equals(
                displayPeriod.getDisplayStartDate(), that.displayPeriod.getDisplayStartDate()))
            return false;
        if (!Objects.equals(
                displayPeriod.getDisplayEndDate(), that.displayPeriod.getDisplayStartDate()))
            return false;
        if (displayOrder != that.displayOrder) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                tabName,
                stickyYn,
                tabMovingType,
                displayPeriod.getDisplayStartDate(),
                displayPeriod.getDisplayEndDate(),
                displayOrder);
    }
}
