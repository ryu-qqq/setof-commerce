package com.connectly.partnerAdmin.module.display.service.component.fetch;


import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQuery;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NonProductComponentFetchStrategy extends AbstractProvider<ComponentType, SubNonProductComponentFetchService<? extends ComponentQuery>> {

    public NonProductComponentFetchStrategy(List<SubNonProductComponentFetchService<? extends ComponentQuery>> services) {
        for (SubNonProductComponentFetchService<? extends ComponentQuery> service : services) {
            map.put(service.getComponentType(), service);
        }
    }
}


