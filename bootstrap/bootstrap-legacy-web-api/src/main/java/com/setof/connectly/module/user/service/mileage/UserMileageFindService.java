package com.setof.connectly.module.user.service.mileage;

import com.setof.connectly.module.mileage.dto.filter.MileageFilter;
import com.setof.connectly.module.mileage.dto.page.MileagePage;
import com.setof.connectly.module.user.dto.mileage.UserMileageDto;
import com.setof.connectly.module.user.dto.mileage.UserMileageHistoryDto;
import org.springframework.data.domain.Pageable;

public interface UserMileageFindService {
    UserMileageDto fetchUserMileage(long userId);

    MileagePage<UserMileageHistoryDto> fetchMyMileageHistories(
            MileageFilter filter, Pageable pageable);

    void hasMileageEnough(long userId, long mileageAmount);
}
