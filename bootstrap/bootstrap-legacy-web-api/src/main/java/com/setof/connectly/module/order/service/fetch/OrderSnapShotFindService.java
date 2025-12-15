package com.setof.connectly.module.order.service.fetch;

import com.setof.connectly.module.order.dto.snapshot.OrderProductSnapShotQueryDto;
import java.util.List;

public interface OrderSnapShotFindService {
    List<OrderProductSnapShotQueryDto> fetchOrderProductForSnapShot(long paymentId);
}
