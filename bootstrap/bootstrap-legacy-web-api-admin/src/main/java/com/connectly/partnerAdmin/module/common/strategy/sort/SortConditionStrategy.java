package com.connectly.partnerAdmin.module.common.strategy.sort;

import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.connectly.partnerAdmin.module.common.enums.SortField;
import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.common.strategy.search.SearchCondition;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SortConditionStrategy extends AbstractProvider<SortField, SortCondition> {

    public SortConditionStrategy(List<SortCondition> services) {
        for (SortCondition service : services) {
            map.put(service.getSortField(), service);
        }
    }
}
