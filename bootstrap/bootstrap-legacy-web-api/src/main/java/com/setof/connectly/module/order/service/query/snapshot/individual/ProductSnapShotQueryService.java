package com.setof.connectly.module.order.service.query.snapshot.individual;

import com.setof.connectly.module.order.entity.snapshot.group.OrderSnapShotProduct;
import com.setof.connectly.module.order.enums.SnapShotEnum;
import com.setof.connectly.module.order.repository.snapshot.query.individual.OrderSnapShotProductJdbcRepository;
import com.setof.connectly.module.order.service.query.snapshot.OrderSnapShotService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSnapShotQueryService implements OrderSnapShotService<OrderSnapShotProduct> {

    private final OrderSnapShotProductJdbcRepository orderSnapShotProductJdbcRepository;

    @Override
    public void saveSnapShot(Set<OrderSnapShotProduct> snapShots) {
        orderSnapShotProductJdbcRepository.saveAll(snapShots);
    }

    @Override
    public SnapShotEnum getSnapShotEnum() {
        return SnapShotEnum.PRODUCT;
    }
}
