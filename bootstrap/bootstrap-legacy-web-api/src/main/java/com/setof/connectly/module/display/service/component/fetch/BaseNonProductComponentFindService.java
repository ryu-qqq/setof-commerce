package com.setof.connectly.module.display.service.component.fetch;

import com.setof.connectly.module.display.dto.query.ComponentQuery;
import com.setof.connectly.module.display.factory.ComponentFactoryStrategy;
import com.setof.connectly.module.display.service.component.SubNonProductComponentFindService;

public abstract class BaseNonProductComponentFindService<T extends ComponentQuery>
        extends BaseComponentFindService implements SubNonProductComponentFindService<T> {

    public BaseNonProductComponentFindService(ComponentFactoryStrategy componentFactoryStrategy) {
        super(componentFactoryStrategy);
    }
}
