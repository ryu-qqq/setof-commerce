package com.setof.connectly.module.order.service.query.snapshot.notice;

import com.setof.connectly.module.order.entity.snapshot.notice.OrderSnapShotProductNotice;
import com.setof.connectly.module.order.enums.SnapShotEnum;
import com.setof.connectly.module.order.repository.snapshot.query.notice.OrderSnapShotProductNoticeJdbcRepository;
import com.setof.connectly.module.order.service.query.snapshot.OrderSnapShotService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductNoticeSnapShotQueryService
        implements OrderSnapShotService<OrderSnapShotProductNotice> {

    private final OrderSnapShotProductNoticeJdbcRepository orderSnapShotProductNoticeJdbcRepository;

    @Override
    public void saveSnapShot(Set<OrderSnapShotProductNotice> snapShots) {
        orderSnapShotProductNoticeJdbcRepository.saveAll(snapShots);
    }

    @Override
    public SnapShotEnum getSnapShotEnum() {
        return SnapShotEnum.PRODUCT_NOTICE;
    }
}
