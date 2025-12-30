package com.connectly.partnerAdmin.module.order.mapper;


import com.connectly.partnerAdmin.module.order.dto.OrderSnapShotQueryDto;
import com.connectly.partnerAdmin.module.order.entity.Order;

public interface OrderSnapShotMapper {
    void setSnapShot(Order order, OrderSnapShotQueryDto orderSnapShotQueryDto);
}
