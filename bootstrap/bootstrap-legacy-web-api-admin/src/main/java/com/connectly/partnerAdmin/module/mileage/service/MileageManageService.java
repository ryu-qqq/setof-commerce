package com.connectly.partnerAdmin.module.mileage.service;

import com.connectly.partnerAdmin.module.order.entity.snapshot.mileage.OrderSnapShotMileageDetail;

import java.util.List;

public interface MileageManageService {

    void refundMileages(long userId, List<OrderSnapShotMileageDetail> mileageDetails);
    void expireMileage();

}
