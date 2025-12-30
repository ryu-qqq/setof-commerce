package com.connectly.partnerAdmin.module.mileage.repopsitory;


import com.connectly.partnerAdmin.module.mileage.entity.Mileage;

import java.util.List;

public interface MileageFetchRepository {

    List<Mileage> fetchMileages(List<Long> mileageIds);
    List<Mileage> fetchMileages(long orderId);

    List<Mileage> fetchExpireMileageEntities();

}
