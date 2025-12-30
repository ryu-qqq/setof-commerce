package com.connectly.partnerAdmin.module.common.strategy.sort;

import com.connectly.partnerAdmin.module.common.enums.SortField;
import com.connectly.partnerAdmin.module.display.entity.content.QContent;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.stereotype.Component;

import static com.connectly.partnerAdmin.module.common.enums.SortField.CONTENT_ID;
import static com.connectly.partnerAdmin.module.display.entity.content.QContent.content;

@Component
public class ContentIdSortCondition extends AbstractSortCondition{

    @Override
    public OrderSpecifier<?> apply(Order direction) {
        return getSortedColumn(direction, content, getSortField().getField());
    }

    @Override
    public SortField getSortField() {
        return CONTENT_ID;
    }

}
