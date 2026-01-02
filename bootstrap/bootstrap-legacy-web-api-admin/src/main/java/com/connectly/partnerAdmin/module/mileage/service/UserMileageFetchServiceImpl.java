package com.connectly.partnerAdmin.module.mileage.service;

import com.connectly.partnerAdmin.module.mileage.exception.MileageNotFoundException;
import com.connectly.partnerAdmin.module.mileage.repopsitory.MileageHistoryFetchRepository;
import com.connectly.partnerAdmin.module.user.entity.UserMileage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserMileageFetchServiceImpl implements UserMileageFetchService {

    private final MileageHistoryFetchRepository mileageHistoryFetchRepository;

    @Override
    public UserMileage fetchUserMileageEntity(long userId) {
        return mileageHistoryFetchRepository.fetchUserMileageEntity(userId).orElseThrow(MileageNotFoundException::new);
    }

}
