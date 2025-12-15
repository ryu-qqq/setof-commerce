package com.setof.connectly.module.order.repository.snapshot.fetch;

import com.setof.connectly.module.order.dto.snapshot.OrderProductSnapShotQueryDto;
import java.util.List;

public interface OrderSnapShotFindRepository {

    List<OrderProductSnapShotQueryDto> fetchSnapShotAboutProduct(long paymentId);
}
