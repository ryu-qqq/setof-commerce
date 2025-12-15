package com.setof.connectly.module.order.service.query.snapshot.option;

import com.setof.connectly.module.order.entity.snapshot.option.OrderSnapShotProductOption;
import com.setof.connectly.module.order.enums.SnapShotEnum;
import com.setof.connectly.module.order.repository.snapshot.query.option.OrderSnapShotProductOptionJdbcRepository;
import com.setof.connectly.module.order.service.query.snapshot.OrderSnapShotService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSnapShotProductOptionQueryService
        implements OrderSnapShotService<OrderSnapShotProductOption> {

    private final OrderSnapShotProductOptionJdbcRepository orderSnapShotProductOptionJdbcRepository;

    @Override
    public void saveSnapShot(Set<OrderSnapShotProductOption> snapShots) {
        orderSnapShotProductOptionJdbcRepository.saveAll(snapShots);
    }

    @Override
    public SnapShotEnum getSnapShotEnum() {
        return SnapShotEnum.PRODUCT_OPTION;
    }
}
