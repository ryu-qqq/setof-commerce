package com.connectly.partnerAdmin.module.display.service.component.fetch;

import com.connectly.partnerAdmin.module.display.dto.query.ComponentQuery;
import com.connectly.partnerAdmin.module.display.factory.ComponentFactoryStrategy;

public abstract class BaseNonProductComponentFetchService<T extends ComponentQuery> extends BaseComponentFetchService implements SubNonProductComponentFetchService<T> {


    public BaseNonProductComponentFetchService(ComponentFactoryStrategy componentFactoryStrategy) {
        super(componentFactoryStrategy);
    }

}