package com.connectly.partnerAdmin.module.external.service.order;

import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.external.core.ExMallClaimOrder;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExternalClaimOrderOrderProvider<T extends ExMallClaimOrder> extends AbstractProvider<SiteName, ExternalClaimOrderService<T>> {

    public ExternalClaimOrderOrderProvider(List<ExternalClaimOrderService<T>> services) {
        for(ExternalClaimOrderService<T> service : services){
            map.put(service.getSiteName(), service);
        }
    }

}
