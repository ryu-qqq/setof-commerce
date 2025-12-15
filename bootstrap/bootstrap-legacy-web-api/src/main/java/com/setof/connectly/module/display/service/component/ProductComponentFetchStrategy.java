package com.setof.connectly.module.display.service.component;

import com.setof.connectly.module.common.provider.AbstractProvider;
import com.setof.connectly.module.display.dto.query.ComponentQuery;
import com.setof.connectly.module.display.enums.component.ComponentType;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProductComponentFetchStrategy
        extends AbstractProvider<
                ComponentType, SubProductComponentFindService<? extends ComponentQuery>> {

    public ProductComponentFetchStrategy(
            List<SubProductComponentFindService<? extends ComponentQuery>> services) {
        for (SubProductComponentFindService<? extends ComponentQuery> service : services) {
            map.put(service.getComponentType(), service);
        }
    }
}
