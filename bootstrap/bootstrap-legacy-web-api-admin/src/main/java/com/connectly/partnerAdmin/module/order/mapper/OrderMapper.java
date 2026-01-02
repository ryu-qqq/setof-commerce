package com.connectly.partnerAdmin.module.order.mapper;

import com.connectly.partnerAdmin.module.order.dto.OrderSnapShotQueryDto;
import com.connectly.partnerAdmin.module.order.dto.query.OrderSheet;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.payment.entity.Payment;

public interface OrderMapper {

    Order toOrder(Payment payment, OrderSheet createOrder, OrderSnapShotQueryDto orderSnapShotQueryDto);

}
