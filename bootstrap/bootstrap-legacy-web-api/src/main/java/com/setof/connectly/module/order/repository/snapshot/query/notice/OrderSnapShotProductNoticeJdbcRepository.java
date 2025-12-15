package com.setof.connectly.module.order.repository.snapshot.query.notice;

import com.setof.connectly.module.order.entity.snapshot.notice.OrderSnapShotProductNotice;
import java.util.Set;

public interface OrderSnapShotProductNoticeJdbcRepository {

    void saveAll(Set<OrderSnapShotProductNotice> orderSnapShotProductNotices);
}
