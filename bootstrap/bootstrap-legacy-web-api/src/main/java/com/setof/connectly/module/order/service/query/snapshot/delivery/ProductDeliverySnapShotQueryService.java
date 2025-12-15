package com.setof.connectly.module.order.service.query.snapshot.delivery;

import com.setof.connectly.module.order.entity.snapshot.delivery.OrderSnapShotProductDelivery;
import com.setof.connectly.module.order.enums.SnapShotEnum;
import com.setof.connectly.module.order.repository.snapshot.query.delivery.OrderSnapShotProductDeliveryJdbcRepository;
import com.setof.connectly.module.order.service.query.snapshot.OrderSnapShotService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductDeliverySnapShotQueryService
        implements OrderSnapShotService<OrderSnapShotProductDelivery> {

    private final OrderSnapShotProductDeliveryJdbcRepository
            orderSnapShotProductDeliveryJdbcRepository;

    @Override
    public void saveSnapShot(Set<OrderSnapShotProductDelivery> snapShots) {
        orderSnapShotProductDeliveryJdbcRepository.saveAll(snapShots);
    }

    @Override
    public SnapShotEnum getSnapShotEnum() {
        return SnapShotEnum.PRODUCT_DELIVERY;
    }
}
