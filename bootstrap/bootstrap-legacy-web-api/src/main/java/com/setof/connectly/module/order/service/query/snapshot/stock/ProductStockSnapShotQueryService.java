package com.setof.connectly.module.order.service.query.snapshot.stock;

import com.setof.connectly.module.order.entity.snapshot.stock.OrderSnapShotProductStock;
import com.setof.connectly.module.order.enums.SnapShotEnum;
import com.setof.connectly.module.order.repository.snapshot.query.stock.OrderSnapShotProductStockJdbcRepository;
import com.setof.connectly.module.order.service.query.snapshot.OrderSnapShotService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductStockSnapShotQueryService
        implements OrderSnapShotService<OrderSnapShotProductStock> {

    private final OrderSnapShotProductStockJdbcRepository orderSnapShotProductStockJdbcRepository;

    @Override
    public void saveSnapShot(Set<OrderSnapShotProductStock> snapShots) {
        orderSnapShotProductStockJdbcRepository.saveAll(snapShots);
    }

    @Override
    public SnapShotEnum getSnapShotEnum() {
        return SnapShotEnum.STOCK;
    }
}
