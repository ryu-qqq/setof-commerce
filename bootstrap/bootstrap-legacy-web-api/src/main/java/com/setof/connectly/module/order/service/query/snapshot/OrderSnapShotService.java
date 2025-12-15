package com.setof.connectly.module.order.service.query.snapshot;

import com.setof.connectly.module.order.dto.snapshot.OrderSnapShot;
import com.setof.connectly.module.order.enums.SnapShotEnum;
import java.util.Set;

public interface OrderSnapShotService<T extends OrderSnapShot> {

    void saveSnapShot(Set<T> snapShots);

    SnapShotEnum getSnapShotEnum();
}
