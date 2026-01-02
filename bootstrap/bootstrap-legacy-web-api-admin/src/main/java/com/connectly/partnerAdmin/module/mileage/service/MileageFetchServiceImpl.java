package com.connectly.partnerAdmin.module.mileage.service;


import com.connectly.partnerAdmin.module.mileage.entity.Mileage;
import com.connectly.partnerAdmin.module.mileage.repopsitory.MileageFetchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MileageFetchServiceImpl implements MileageFetchService {
    private final MileageFetchRepository mileageFetchRepository;


    @Override
    public List<Mileage> fetchMileageEntities(List<Long> mileageIds) {
        return mileageFetchRepository.fetchMileages(mileageIds);
    }

    @Override
    public List<Mileage> fetchMileageEntities(long orderId) {
        return mileageFetchRepository.fetchMileages(orderId);
    }

    @Override
    public List<Mileage> fetchExpireMileageEntities() {
        return mileageFetchRepository.fetchExpireMileageEntities();
    }

}
