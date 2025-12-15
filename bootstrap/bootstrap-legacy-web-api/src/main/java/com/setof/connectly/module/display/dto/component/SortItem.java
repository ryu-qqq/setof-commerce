package com.setof.connectly.module.display.dto.component;

import com.setof.connectly.module.display.enums.SortType;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.LinkedList;
import java.util.List;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SortItem {

    private SortType sortType;
    private List<ProductGroupThumbnail> productGroups;

    public SortItem(SortType sortType) {
        this.sortType = sortType;
        this.productGroups = new LinkedList<>();
    }

    public boolean hasProductGroups() {
        return !productGroups.isEmpty();
    }
}
