package com.connectly.partnerAdmin.module.mileage.service;

import com.connectly.partnerAdmin.module.order.entity.Order;

public interface MileagePublishService {
    void publicMileage(Order order);
}
