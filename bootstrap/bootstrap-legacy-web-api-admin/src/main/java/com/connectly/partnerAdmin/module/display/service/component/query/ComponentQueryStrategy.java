package com.connectly.partnerAdmin.module.display.service.component.query;

import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ComponentQueryStrategy extends AbstractProvider<ComponentType, SubComponentQueryService<? extends SubComponent>> {


    public ComponentQueryStrategy(List<SubComponentQueryService<? extends SubComponent>> services) {
        for (SubComponentQueryService<? extends SubComponent> service : services) {
            map.put(service.getComponentType(), service);
        }
    }

}
