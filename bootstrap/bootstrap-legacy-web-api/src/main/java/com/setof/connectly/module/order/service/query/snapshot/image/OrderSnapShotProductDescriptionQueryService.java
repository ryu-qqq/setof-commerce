package com.setof.connectly.module.order.service.query.snapshot.image;

import com.setof.connectly.module.order.entity.snapshot.image.OrderSnapShotProductGroupDetailDescription;
import com.setof.connectly.module.order.enums.SnapShotEnum;
import com.setof.connectly.module.order.repository.snapshot.query.image.OrderSnapShotProductDescriptionJdbcRepository;
import com.setof.connectly.module.order.service.query.snapshot.OrderSnapShotService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSnapShotProductDescriptionQueryService
        implements OrderSnapShotService<OrderSnapShotProductGroupDetailDescription> {

    private final OrderSnapShotProductDescriptionJdbcRepository
            orderSnapShotProductDescriptionJdbcRepository;

    @Override
    public void saveSnapShot(Set<OrderSnapShotProductGroupDetailDescription> snapShots) {
        orderSnapShotProductDescriptionJdbcRepository.saveAll(snapShots);
    }

    @Override
    public SnapShotEnum getSnapShotEnum() {
        return SnapShotEnum.PRODUCT_DESCRIPTION;
    }
}
