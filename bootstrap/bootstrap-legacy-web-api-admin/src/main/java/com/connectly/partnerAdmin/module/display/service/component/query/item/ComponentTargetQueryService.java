package com.connectly.partnerAdmin.module.display.service.component.query.item;

import com.connectly.partnerAdmin.module.display.entity.component.ComponentTarget;

import java.util.List;

public interface ComponentTargetQueryService {

    ComponentTarget saveComponentTarget(ComponentTarget componentTarget);

    void deleteComponentTarget(List<Long> ComponentTargetId);

    void deleteComponentTargetWithTabIds(List<Long> tabIds);

}
