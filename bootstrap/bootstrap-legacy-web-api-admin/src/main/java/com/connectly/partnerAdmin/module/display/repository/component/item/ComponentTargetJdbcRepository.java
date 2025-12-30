package com.connectly.partnerAdmin.module.display.repository.component.item;

import java.util.List;

public interface ComponentTargetJdbcRepository {

    void deleteAll(List<Long> componentTargetIds);

    void deleteAllWithTabIds(List<Long> tabIds);
}
