package com.connectly.partnerAdmin.module.external.service.order.oco;

import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OcoOrderUpdateServiceProvider <T extends UpdateOrder> extends AbstractProvider<OrderStatus, OcoOrderUpdateService<T>> {

    public OcoOrderUpdateServiceProvider(List<OcoOrderUpdateService<T>> services) {
        for(OcoOrderUpdateService<T> service : services){
            map.put(service.getOrderStatus(), service);
        }
    }

}
