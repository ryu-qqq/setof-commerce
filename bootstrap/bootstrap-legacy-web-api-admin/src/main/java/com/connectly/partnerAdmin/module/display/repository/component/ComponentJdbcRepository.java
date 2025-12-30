package com.connectly.partnerAdmin.module.display.repository.component;

import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;

import java.util.List;

public interface ComponentJdbcRepository {

    void deleteAll(List<Long> componentIds);

    void updateAll(List<SubComponent> components);
}
