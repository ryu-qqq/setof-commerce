package com.connectly.partnerAdmin.module.mileage.repopsitory;


import com.connectly.partnerAdmin.module.user.entity.UserMileage;

import java.util.Optional;

public interface MileageHistoryFetchRepository {

    Optional<UserMileage> fetchUserMileageEntity(long userId);

}
