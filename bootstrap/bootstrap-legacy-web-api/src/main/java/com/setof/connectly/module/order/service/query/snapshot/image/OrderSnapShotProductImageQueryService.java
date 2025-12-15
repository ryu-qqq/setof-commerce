package com.setof.connectly.module.order.service.query.snapshot.image;

import com.setof.connectly.module.order.entity.snapshot.image.OrderSnapShotProductGroupImage;
import com.setof.connectly.module.order.enums.SnapShotEnum;
import com.setof.connectly.module.order.repository.snapshot.query.image.OrderSnapShotProductGroupImageJdbcRepository;
import com.setof.connectly.module.order.service.query.snapshot.OrderSnapShotService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSnapShotProductImageQueryService
        implements OrderSnapShotService<OrderSnapShotProductGroupImage> {

    private final OrderSnapShotProductGroupImageJdbcRepository
            orderSnapShotProductGroupImageJdbcRepository;

    @Override
    public void saveSnapShot(Set<OrderSnapShotProductGroupImage> snapShots) {
        orderSnapShotProductGroupImageJdbcRepository.saveAll(snapShots);
    }

    @Override
    public SnapShotEnum getSnapShotEnum() {
        return SnapShotEnum.PRODUCT_IMAGE;
    }
}
