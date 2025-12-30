package com.connectly.partnerAdmin.module.display.service.component.query;

import com.connectly.partnerAdmin.module.display.dto.component.ComponentUpdatePair;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;

import java.util.List;


public interface SubComponentQueryService<T extends SubComponent> {

    ComponentType getComponentType();

    void createComponents(long contentId, List<T> components);

    void removeComponents(List<Long> subComponentIds);

    void updateComponents(List<ComponentUpdatePair<T>> components);

}
