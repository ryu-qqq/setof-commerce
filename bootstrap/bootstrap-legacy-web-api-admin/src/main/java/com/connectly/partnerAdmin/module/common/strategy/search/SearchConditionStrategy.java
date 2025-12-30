package com.connectly.partnerAdmin.module.common.strategy.search;

import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchConditionStrategy extends AbstractProvider<SearchKeyword, SearchCondition> {

    public SearchConditionStrategy(List<SearchCondition> services) {
        for (SearchCondition service : services) {
            map.put(service.getSearchKeyword(), service);
        }
    }
}
