package com.connectly.partnerAdmin.module.common.strategy.search;

import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.BANNER_NAME;
import static com.connectly.partnerAdmin.module.display.entity.component.item.QBanner.banner;

@Component
public class BannerTitleSearchCondition extends AbstractSearchCondition{

    @Override
    public BooleanExpression apply(String searchWord) {
        return banner.title.like("%" + searchWord + "%");
    }

    @Override
    public SearchKeyword getSearchKeyword() {
        return BANNER_NAME;
    }
}
