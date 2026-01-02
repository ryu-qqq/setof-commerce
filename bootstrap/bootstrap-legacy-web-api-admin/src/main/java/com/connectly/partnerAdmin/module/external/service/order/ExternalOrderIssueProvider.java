package com.connectly.partnerAdmin.module.external.service.order;


import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.external.core.ExMallOrder;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExternalOrderIssueProvider<T extends ExMallOrder> extends AbstractProvider<SiteName, ExternalOrderService<T>> {

    public ExternalOrderIssueProvider(List<ExternalOrderService<T>> services) {
        for(ExternalOrderService<T> service : services){
            map.put(service.getSiteName(), service);
        }
    }

}
