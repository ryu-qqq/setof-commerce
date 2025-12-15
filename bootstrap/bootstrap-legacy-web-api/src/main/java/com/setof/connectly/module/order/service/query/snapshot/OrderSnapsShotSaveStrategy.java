package com.setof.connectly.module.order.service.query.snapshot;

import com.setof.connectly.module.common.provider.AbstractProvider;
import com.setof.connectly.module.order.dto.snapshot.OrderSnapShot;
import com.setof.connectly.module.order.enums.SnapShotEnum;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderSnapsShotSaveStrategy<T extends OrderSnapShot>
        extends AbstractProvider<SnapShotEnum, OrderSnapShotService<T>> {

    public OrderSnapsShotSaveStrategy(List<OrderSnapShotService<T>> services) {
        for (OrderSnapShotService<T> service : services) {
            map.put(service.getSnapShotEnum(), service);
        }
    }
}
