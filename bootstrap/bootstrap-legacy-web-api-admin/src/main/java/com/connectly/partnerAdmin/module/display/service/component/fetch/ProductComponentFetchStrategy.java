package com.connectly.partnerAdmin.module.display.service.component.fetch;


import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQuery;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductComponentFetchStrategy extends AbstractProvider<ComponentType, SubProductComponentFetchService<? extends ComponentQuery>> {

    public ProductComponentFetchStrategy(List<SubProductComponentFetchService<? extends ComponentQuery>> services) {
        for (SubProductComponentFetchService<? extends ComponentQuery> service : services) {
            map.put(service.getComponentType(), service);
        }
    }
}


