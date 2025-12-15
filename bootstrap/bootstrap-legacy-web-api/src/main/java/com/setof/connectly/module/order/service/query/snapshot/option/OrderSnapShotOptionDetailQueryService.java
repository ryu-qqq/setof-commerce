package com.setof.connectly.module.order.service.query.snapshot.option;

import com.setof.connectly.module.order.entity.snapshot.option.OrderSnapShotOptionDetail;
import com.setof.connectly.module.order.enums.SnapShotEnum;
import com.setof.connectly.module.order.repository.snapshot.query.option.OrderSnapShotOptionDetailJdbcRepository;
import com.setof.connectly.module.order.service.query.snapshot.OrderSnapShotService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSnapShotOptionDetailQueryService
        implements OrderSnapShotService<OrderSnapShotOptionDetail> {

    private final OrderSnapShotOptionDetailJdbcRepository orderSnapShotOptionDetailJdbcRepository;

    @Override
    public void saveSnapShot(Set<OrderSnapShotOptionDetail> snapShots) {
        orderSnapShotOptionDetailJdbcRepository.saveAll(snapShots);
    }

    @Override
    public SnapShotEnum getSnapShotEnum() {
        return SnapShotEnum.OPTION_DETAIL;
    }
}
