package com.setof.connectly.module.user.repository.mileage;

import com.setof.connectly.module.mileage.dto.query.PendingMileageDto;
import com.setof.connectly.module.mileage.dto.query.UserMileageQueryDto;
import java.util.List;

public interface UserMileageFindRepository {

    List<UserMileageQueryDto> fetchUserMileageQueryInMyPage(long userId);

    List<PendingMileageDto> fetchUserPendingMileages(long userId);
}
