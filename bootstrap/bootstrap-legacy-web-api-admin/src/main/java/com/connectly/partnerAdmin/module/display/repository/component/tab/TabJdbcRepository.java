package com.connectly.partnerAdmin.module.display.repository.component.tab;

import com.connectly.partnerAdmin.module.display.dto.component.tab.TabDetail;
import com.connectly.partnerAdmin.module.display.entity.component.item.Tab;

import java.util.List;

public interface TabJdbcRepository {

    void saveAll(List<Tab> tabs);
    void update(TabDetail tabDetail);
    void deleteAllWithTabComponentIds(List<Long> tabComponentIds);
    void deleteAll(List<Long> tabIds);
}
