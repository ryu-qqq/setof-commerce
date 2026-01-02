package com.connectly.partnerAdmin.module.mileage.service;

import com.connectly.partnerAdmin.module.mileage.entity.Mileage;

import java.util.List;

public interface MileageFetchService {

    List<Mileage> fetchMileageEntities(List<Long> mileageIds);
    List<Mileage> fetchMileageEntities(long orderId);
    List<Mileage> fetchExpireMileageEntities();

}
