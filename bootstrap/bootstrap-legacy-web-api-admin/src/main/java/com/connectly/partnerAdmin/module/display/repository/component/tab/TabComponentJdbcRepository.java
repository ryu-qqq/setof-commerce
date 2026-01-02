package com.connectly.partnerAdmin.module.display.repository.component.tab;

import com.connectly.partnerAdmin.module.display.dto.component.tab.TabDetail;

import java.util.List;

public interface TabComponentJdbcRepository {

    void deleteAll(List<Long> tabComponentIds);
    void update(long tabComponentId, TabDetail tabDetail);
}
