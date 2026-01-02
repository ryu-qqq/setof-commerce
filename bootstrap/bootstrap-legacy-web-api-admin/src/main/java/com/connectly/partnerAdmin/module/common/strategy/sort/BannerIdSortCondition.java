package com.connectly.partnerAdmin.module.common.strategy.sort;

import com.connectly.partnerAdmin.module.common.enums.SortField;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.stereotype.Component;

import static com.connectly.partnerAdmin.module.common.enums.SortField.BANNER_ID;
import static com.connectly.partnerAdmin.module.display.entity.component.item.QBanner.banner;

@Component
public class BannerIdSortCondition extends AbstractSortCondition{

    @Override
    public OrderSpecifier<?> apply(Order direction) {
        return getSortedColumn(direction, banner, getSortField().getField());
    }

    @Override
    public SortField getSortField() {
        return BANNER_ID;
    }

}
