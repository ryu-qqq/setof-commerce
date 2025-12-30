package com.connectly.partnerAdmin.module.mileage.service;


import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.mileage.entity.Mileage;
import com.connectly.partnerAdmin.module.order.entity.snapshot.mileage.OrderSnapShotMileageDetail;
import com.connectly.partnerAdmin.module.user.entity.UserMileage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Transactional
@Service
@RequiredArgsConstructor
public class MileageManageServiceImpl implements MileageManageService{

    private final MileageFetchService mileageFetchService;
    private final UserMileageFetchService userMileageFetchService;

    @Override
    public void refundMileages(long userId, List<OrderSnapShotMileageDetail> mileageDetails) {
        Map<Long, OrderSnapShotMileageDetail> mileageIdMap = mileageDetails.stream()
                .collect(Collectors.toMap(OrderSnapShotMileageDetail::getMileageId, Function.identity(),
                        (v1, v2) -> v1));
        List<Mileage> mileages = mileageFetchService.fetchMileageEntities(new ArrayList<>(mileageIdMap.keySet()));

        UserMileage userMileage = userMileageFetchService.fetchUserMileageEntity(userId);

        for (Mileage mileage : mileages) {
            if(mileage.isValid()){
                OrderSnapShotMileageDetail detail = mileageIdMap.get(mileage.getId());
                Money usedAmount = Money.wons(detail.getUsedAmount());
                userMileage.refundMileage(mileage, usedAmount, detail.getOrderSnapShotMileage().getOrder().getId());
            }
        }
    }

    @Override
    public void expireMileage(){
        List<Mileage> mileages = mileageFetchService.fetchExpireMileageEntities();
        mileages.forEach(Mileage::expire);
    }

}
