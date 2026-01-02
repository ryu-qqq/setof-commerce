package com.connectly.partnerAdmin.module.external.service.order.sewon;

import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SellicOrderUpdateServiceProvider <T extends UpdateOrder> extends AbstractProvider<OrderStatus, SellicOrderUpdateService<T>> {

    public SellicOrderUpdateServiceProvider(List<SellicOrderUpdateService<T>> services) {
        for(SellicOrderUpdateService<T> service : services){
            map.put(service.getOrderStatus(), service);
        }
    }


}
