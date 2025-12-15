package com.setof.connectly.module.user.mapper.mileage;

import com.setof.connectly.module.mileage.dto.page.MileagePage;
import com.setof.connectly.module.mileage.dto.query.UserMileageQueryDto;
import com.setof.connectly.module.user.dto.mileage.UserMileageDto;
import com.setof.connectly.module.user.dto.mileage.UserMileageHistoryDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface UserMileageMapper {
    void setTitle(List<UserMileageHistoryDto> userMileageHistories);

    UserMileageDto toMyMileages(
            long userId, List<UserMileageQueryDto> mileageQueries, double pendingMileages);

    MileagePage<UserMileageHistoryDto> toMileagePage(
            UserMileageDto userMileage, Page<UserMileageHistoryDto> page);
}
