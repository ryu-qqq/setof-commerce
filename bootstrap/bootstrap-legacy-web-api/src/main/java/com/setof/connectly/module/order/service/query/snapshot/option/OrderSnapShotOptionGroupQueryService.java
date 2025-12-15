package com.setof.connectly.module.order.service.query.snapshot.option;

import com.setof.connectly.module.order.entity.snapshot.option.OrderSnapShotOptionGroup;
import com.setof.connectly.module.order.enums.SnapShotEnum;
import com.setof.connectly.module.order.repository.snapshot.query.option.OrderSnapShotOptionGroupJdbcRepository;
import com.setof.connectly.module.order.service.query.snapshot.OrderSnapShotService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSnapShotOptionGroupQueryService
        implements OrderSnapShotService<OrderSnapShotOptionGroup> {

    private final OrderSnapShotOptionGroupJdbcRepository orderSnapShotOptionGroupJdbcRepository;

    @Override
    public void saveSnapShot(Set<OrderSnapShotOptionGroup> snapShots) {
        orderSnapShotOptionGroupJdbcRepository.saveAll(snapShots);
    }

    @Override
    public SnapShotEnum getSnapShotEnum() {
        return SnapShotEnum.OPTION_GROUP;
    }
}
